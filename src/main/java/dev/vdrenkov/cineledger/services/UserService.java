package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import dev.vdrenkov.cineledger.exceptions.NotLoggedInException;
import dev.vdrenkov.cineledger.exceptions.RoleNotChosenException;
import dev.vdrenkov.cineledger.exceptions.UserEmailAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.UserNotFoundException;
import dev.vdrenkov.cineledger.exceptions.UsernameAlreadyExistsException;
import dev.vdrenkov.cineledger.jwt.JwtCookieUtil;
import dev.vdrenkov.cineledger.mappers.UserMapper;
import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.models.requests.AdminRequest;
import dev.vdrenkov.cineledger.models.requests.LoginRequest;
import dev.vdrenkov.cineledger.models.requests.UserRequest;
import dev.vdrenkov.cineledger.models.requests.CreateUserRequest;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import dev.vdrenkov.cineledger.utils.constants.RoleConstants;
import dev.vdrenkov.cineledger.utils.constants.UserConstants;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtCookieUtil jwtCookieUtil;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(final AuthenticationManager authenticationManager, final BCryptPasswordEncoder passwordEncoder,
        final EmailService emailService, final JwtCookieUtil jwtCookieUtil, final UserRepository userRepository,
        final RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtCookieUtil = jwtCookieUtil;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public HttpCookie login(final LoginRequest request) {
        final UserDetails userDetails = (UserDetails) authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()))
            .getPrincipal();

        return jwtCookieUtil.createJWTCookie(userDetails);
    }

    @Transactional
    public User createUser(final CreateUserRequest request) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final boolean administrator = authentication != null && authentication.getAuthorities().stream()
            .anyMatch(authority -> RoleConstants.DEFAULT_ADMIN_ROLE.equals(authority.getAuthority()));

        if (request.roleNames() != null && !administrator) {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        userValidation(request.username(), request.email());
        final List<Role> roles = request.roleNames() == null ? getDefaultRoleList() : getUserRoles(request.roleNames());
        final User user = userRepository.save(new User(request.username(), passwordEncoder.encode(request.password()),
            request.email(), request.firstName(), request.lastName(), LocalDate.now(), roles));
        emailService.sendRegistrationConfirmationEmail(user);
        return user;
    }

    public UserDto getProfile(final int id) {

        if (!isCurrentUserAuthorized(id)) {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        return getUserDtoById(id);
    }

    public Role getRoleByName(final String roleName) {

        return roleService.getRoleByName(roleName);
    }

    public List<Role> getDefaultRoleList() {

        return Collections.singletonList(getRoleByName(RoleConstants.DEFAULT_USER_ROLE));
    }

    public List<Role> getUserRoles(final List<String> roleNames) {
        if (roleNames != null && !roleNames.isEmpty()) {

            return roleNames.stream().map(this::getRoleByName).toList();
        } else {
            throw new RoleNotChosenException(ExceptionMessages.ROLE_NOT_CHOSEN_MESSAGE);
        }
    }

    public User getUserById(final int id) {

        return userRepository.findById(id).orElseThrow(() -> {

            return new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
        });
    }

    public UserDto getUserDtoById(final int id) {

        return UserMapper.mapUserToUserDto(getUserById(id));
    }

    public User getUserByUsername(final String username) {

        return userRepository.findUserByUsername(username).orElseThrow(() -> {

            return new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
        });
    }

    public UserDto getUserDtoByUsername(final String username) {

        return UserMapper.mapUserToUserDto(getUserByUsername(username));
    }

    public User getUserByEmail(final String email) {

        return userRepository.findUserByEmail(email).orElseThrow(() -> {

            return new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
        });
    }

    public UserDto getUserDtoByEmail(final String email) {

        return UserMapper.mapUserToUserDto(getUserByEmail(email));
    }

    public List<User> getUsersByRoleName(final String roleName) {

        return userRepository.findAllByRolesName(roleName.toUpperCase());
    }

    public List<UserDto> getUsersDtoByRoleName(final String roleName) {

        return UserMapper.mapUsersToUserDtos(getUsersByRoleName(roleName));
    }

    public List<User> getUsersByJoinDate(final LocalDate joinDate, final boolean isBefore) {
        return isBefore ?
            userRepository.findAllByJoinDateBefore(joinDate) :
            userRepository.findAllByJoinDateAfter(joinDate);
    }

    public List<UserDto> getUsersDtosByJoinDate(final LocalDate joinDate, final boolean isBefore) {
        return UserMapper.mapUsersToUserDtos(getUsersByJoinDate(joinDate, isBefore));
    }

    private User getUserByUsernameOnLogin(final String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> {

            return new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
        });
    }

    public User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String currentUsername = Objects.isNull(authentication) ? "" : authentication.getName();
        return getUserByUsername(currentUsername);
    }

    @Transactional
    public UserDto updateUserByAdmin(final AdminRequest request, final int id) {
        final User user = userRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE));
        final UserDto previous = UserMapper.mapUserToUserDto(user);
        applyProfile(user, new UserRequest(request.username(), request.password(), request.email(), request.firstName(), request.lastName()));
        user.setRoles(getUserRoles(request.roleNames()));
        userRepository.save(user);
        return previous;
    }

    @Transactional
    public UserDto updateUser(final UserRequest request, final int id) {

        if (!isAuthorized(id)) {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        final User user = userRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE));
        final UserDto previous = UserMapper.mapUserToUserDto(user);
        applyProfile(user, request);
        userRepository.save(user);
        return previous;
    }

    public UserDto deleteUser(final int id) {
        final UserDto userDto = getUserDtoById(id);

        if (!isAuthorized(id)) {

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        userRepository.deleteById(id);
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth) && Objects.equals(auth.getName(), userDto.username())) {
            SecurityContextHolder.clearContext();
        }

        return userDto;
    }

    public boolean isCurrentUserAdmin() {
        final User currentUser = getCurrentUser();

        return currentUser
            .getRoles()
            .stream()
            .anyMatch(role -> Objects.equals(RoleConstants.DEFAULT_ADMIN_ROLE, role.getName()));
    }

    public boolean isCurrentUserAuthorized(final int userId) {
        final User currentUser = getCurrentUser();

        if (isCurrentUserAdmin()) {
            return true;
        }

        return currentUser.getId() == userId;
    }

private void applyProfile(final User user, final UserRequest request) {
    user.setUsername(request.username());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setCredentialsVersion(user.getCredentialsVersion() + 1);
    user.setEmail(request.email());
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
}

private boolean isAuthorized(final int userId) {
        String currentUsername = null;

        SecurityContextHolder.getContext();
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.nonNull(auth)) {
                currentUsername = auth.getName();
            }
        }

        if (currentUsername == null || Objects.equals(UserConstants.DEFAULT_GUEST_USERNAME, currentUsername)) {

            throw new NotLoggedInException(ExceptionMessages.NOT_LOGGED_IN_MESSAGE);
        }

        final User currentUser = getUserByUsernameOnLogin(currentUsername);
        final Role adminRole = getRoleByName(RoleConstants.DEFAULT_ADMIN_ROLE);

        for (Role role : currentUser.getRoles()) {
            if (Objects.equals(role.getName(), adminRole.getName())) {
                return true;
            }
        }

        return userId == currentUser.getId();
    }

    private void userValidation(final String username, final String email) {
        userRepository.findUserByUsername(username).ifPresent(user -> {

            throw new UsernameAlreadyExistsException(ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE);
        });

        userRepository.findUserByEmail(email).ifPresent(user -> {

            throw new UserEmailAlreadyExistsException(ExceptionMessages.USER_EMAIL_ALREADY_EXISTS_MESSAGE);
        });
    }

}
