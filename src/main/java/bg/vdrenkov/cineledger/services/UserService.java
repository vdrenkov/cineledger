package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import bg.vdrenkov.cineledger.exceptions.NotLoggedInException;
import bg.vdrenkov.cineledger.exceptions.RoleNotChosenException;
import bg.vdrenkov.cineledger.exceptions.UserEmailAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.UserNotFoundException;
import bg.vdrenkov.cineledger.exceptions.UsernameAlreadyExistsException;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import bg.vdrenkov.cineledger.utils.constants.RoleConstants;
import bg.vdrenkov.cineledger.utils.constants.UserConstants;
import bg.vdrenkov.cineledger.jwt.JwtCookieUtil;
import bg.vdrenkov.cineledger.mappers.UserMapper;
import bg.vdrenkov.cineledger.models.dtos.UserDto;
import bg.vdrenkov.cineledger.models.entities.Role;
import bg.vdrenkov.cineledger.models.entities.User;
import bg.vdrenkov.cineledger.models.requests.AdminRequest;
import bg.vdrenkov.cineledger.models.requests.LoginRequest;
import bg.vdrenkov.cineledger.models.requests.UserRequest;
import bg.vdrenkov.cineledger.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  private static final String RECOVERY_PASSWORD_CHARACTERS =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
  private static final int RECOVERY_PASSWORD_LENGTH = 12;

  private final AuthenticationManager authenticationManager;
  private final BCryptPasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final JwtCookieUtil jwtCookieUtil;
  private final UserRepository userRepository;
  private final Random random;
  private final RoleService roleService;
  private final UserMapper userMapper;

  @Autowired
  public UserService(
    final AuthenticationManager authenticationManager, final BCryptPasswordEncoder passwordEncoder,
    final EmailService emailService, final JwtCookieUtil jwtCookieUtil, final UserRepository userRepository,
    final Random random, final RoleService roleService, final UserMapper userMapper) {
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.jwtCookieUtil = jwtCookieUtil;
    this.userRepository = userRepository;
    this.random = random;
    this.roleService = roleService;
    this.userMapper = userMapper;
  }

  public HttpCookie login(final LoginRequest request) {
    final UserDetails userDetails = (UserDetails) authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())).getPrincipal();

    log.info("A login attempt");

    return jwtCookieUtil.createJWTCookie(userDetails);
  }

  public HttpCookie registerUser(final UserRequest userRequest) {
    addUser(userRequest);

    log.info("An attempt a new user to be registered");

    return login(new LoginRequest(userRequest.getUsername(), userRequest.getPassword()));
  }

  public void registerUserByAdmin(final AdminRequest request) {
    addUserByAdmin(request);
  }

  public User addUser(final UserRequest userRequest) {
    final String password = passwordEncoder.encode(userRequest.getPassword());

    userValidation(userRequest.getUsername(), userRequest.getEmail());

    final User user =
      new User(userRequest.getUsername(), password, userRequest.getEmail(), userRequest.getFirstName(),
               userRequest.getLastName(),
               LocalDate.now(), getDefaultRoleList());

    emailService.sendRegistrationConfirmationEmail(user);

    log.info("Trying to add a new user");


    return userRepository.save(user);
  }

  public User addUserByAdmin(final AdminRequest request) {
    final String password = passwordEncoder.encode(request.getPassword());
    userValidation(request.getUsername(), request.getEmail());

    final User user =
      new User(request.getUsername(), password, request.getEmail(), request.getFirstName(), request.getLastName(),
               LocalDate.now(), getUserRoles(request.getRoleNames()));

    log.info("An attempt a new user to be added by an admin");

    return userRepository.save(user);
  }

  public Role getRoleByName(final String roleName) {
    log.info(String.format("Trying to retrieve the user role %s", roleName));

    return roleService.getRoleByName(roleName);
  }

  public List<Role> getDefaultRoleList() {
    log.info("Trying to retrieve the default user role");

    return Collections.singletonList(getRoleByName(RoleConstants.DEFAULT_USER_ROLE));
  }

  public List<Role> getUserRoles(final List<String> roleNames) {
    if (Objects.nonNull(roleNames) && roleNames.size() > 0) {

      log.info("Trying to map all ids to user roles");

      return roleNames.stream()
                      .map(this::getRoleByName)
                      .collect(Collectors.toList());
    } else {
      throw new RoleNotChosenException(ExceptionMessages.ROLE_NOT_CHOSEN_MESSAGE);
    }
  }

  public User getUserById(final int id) {
    log.info(String.format("Trying to retrieve user with id %d", id));

    return userRepository.findById(id).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", ExceptionMessages.USER_NOT_FOUND_MESSAGE));

      throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
    });
  }

  public UserDto getUserDtoById(final int id) {
    log.info(String.format("Trying to retrieve user DTO with id %d", id));

    return userMapper.mapUserToUserDto(getUserById(id));
  }

  public User getUserByUsername(final String username) {
    log.info(String.format("Trying to retrieve user with username %s", username));

    return userRepository.findUserByUsername(username).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", ExceptionMessages.USER_NOT_FOUND_MESSAGE));

      throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
    });
  }

  public UserDto getUserDtoByUsername(final String username) {
    log.info(String.format("Trying to retrieve user DTO with username %s", username));

    return userMapper.mapUserToUserDto(getUserByUsername(username));
  }

  public User getUserByEmail(final String email) {
    log.info(String.format("Trying to retrieve user with email %s", email));

    return userRepository.findUserByEmail(email).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", ExceptionMessages.USER_NOT_FOUND_MESSAGE));

      throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
    });
  }

  public UserDto getUserDtoByEmail(final String email) {
    log.info(String.format("Trying to retrieve user DTO with email %s", email));

    return userMapper.mapUserToUserDto(getUserByEmail(email));
  }

  public List<User> getUsersByRoleName(final String roleName) {
    log.info(String.format("Trying to retrieve users with role %s", roleName));

    return userRepository.findAllByRolesName(roleName.toUpperCase());
  }

  public List<UserDto> getUsersDtoByRoleName(final String roleName) {
    log.info(String.format("Trying to retrieve user DTOS with role %s", roleName));

    return userMapper.mapUsersToUserDtos(getUsersByRoleName(roleName));
  }

  public List<User> getUsersByJoinDate(final LocalDate joinDate, final boolean isBefore) {
    return isBefore ? userRepository.findAllByJoinDateBefore(joinDate)
                    : userRepository.findAllByJoinDateAfter(joinDate);
  }

  public List<UserDto> getUsersDtosByJoinDate(final LocalDate joinDate, final boolean isBefore) {
    return userMapper.mapUsersToUserDtos(getUsersByJoinDate(joinDate, isBefore));
  }

  private User getUserByUsernameOnLogin(final String username) {
    return userRepository.findUserByUsername(username).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", ExceptionMessages.USER_NOT_FOUND_MESSAGE));

      throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
    });
  }

  public User getCurrentUser() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String currentUsername = authentication.getName();

    return getUserByUsername(currentUsername);
  }

  public UserDto updateUserByAdmin(final AdminRequest request, final int id) {
    final UserDto userDto = getUserDtoById(id);
    final String password = passwordEncoder.encode(request.getPassword());
    final List<Role> roles = getUserRoles(request.getRoleNames());

    userRepository.save(new User(id, request.getUsername(), password, request.getEmail(),
                                 request.getFirstName(), request.getLastName(), userDto.getJoinDate(), roles));

    log.info(String.format("User with id %d was updated by an admin", id));
    return userDto;
  }

  public UserDto updateUser(final UserRequest userRequest, final int id) {
    final User user = getUserById(id);
    final UserDto oldUser = userMapper.mapUserToUserDto(user);
    final String password = passwordEncoder.encode(userRequest.getPassword());

    if (!isAuthorized(id)) {
      log.error(String.format("Exception caught: %s", ExceptionMessages.NOT_AUTHORIZED_MESSAGE));

      throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
    }

    userRepository.save(new User(id, userRequest.getUsername(), password, userRequest.getEmail(),
                                 userRequest.getFirstName(), userRequest.getLastName(), user.getJoinDate(),
                                 user.getRoles()));

    log.info(String.format("User with id %d was updated", id));

    return oldUser;
  }

  public UserDto deleteUser(final int id) {
    final UserDto userDto = getUserDtoById(id);

    if (!isAuthorized(id)) {
      log.error(String.format("Exception caught: %s", ExceptionMessages.NOT_AUTHORIZED_MESSAGE));

      throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
    }

    userRepository.deleteById(id);

    if (Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(), userDto.getUsername())) {
      SecurityContextHolder.clearContext();
    }

    log.info(String.format("User with id %d was deleted", id));

    return userDto;
  }

  public boolean isCurrentUserAdmin() {
    User currentUser = getCurrentUser();

    return currentUser.getRoles().stream()
                      .anyMatch(role -> Objects.equals(RoleConstants.DEFAULT_ADMIN_ROLE, role.getName()));
  }

  public boolean isCurrentUserAuthorized(final int userId) {
    final User currentUser = getCurrentUser();

    if (isCurrentUserAdmin()) {
      return true;
    }

    return currentUser.getId() == userId;
  }

  public User recoverPassword(final String username) {
    final User user = getUserByUsername(username);
    final String newPassword = generateRandomPassword();

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    emailService.sendPasswordConfirmationEmail(user, newPassword);

    log.info(String.format("The user %s password has been changed", username));
    return user;
  }

  private boolean isAuthorized(final int userId) {
    String currentUsername = null;

    if (Objects.nonNull(SecurityContextHolder.getContext()) &&
        Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
      currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    if (Objects.isNull(currentUsername) || Objects.equals(UserConstants.DEFAULT_GUEST_USERNAME, currentUsername)) {
      log.error(String.format("Exception caught: %s", ExceptionMessages.NOT_LOGGED_IN_MESSAGE));

      throw new NotLoggedInException(ExceptionMessages.NOT_LOGGED_IN_MESSAGE);
    }

    final User currentUser = getUserByUsernameOnLogin(currentUsername);
    final Role adminRole = getRoleByName(RoleConstants.DEFAULT_ADMIN_ROLE);

    for (Role role : currentUser.getRoles()) {
      if (Objects.equals(role, adminRole)) {
        return true;
      }
    }

    return userId == currentUser.getId();
  }

  private void userValidation(final String username, final String email) {
    userRepository.findUserByUsername(username).ifPresent(user -> {
      log.error(String.format("Exception caught: %s", ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE));

      throw new UsernameAlreadyExistsException(ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE);
    });

    userRepository.findUserByEmail(email).ifPresent(user -> {
      log.error(String.format("Exception caught: %s", ExceptionMessages.USER_EMAIL_ALREADY_EXISTS_MESSAGE));

      throw new UserEmailAlreadyExistsException(ExceptionMessages.USER_EMAIL_ALREADY_EXISTS_MESSAGE);
    });
  }

  private String generateRandomPassword() {
    final StringBuilder password = new StringBuilder(RECOVERY_PASSWORD_LENGTH);

    for (int i = 0; i < RECOVERY_PASSWORD_LENGTH; i++) {
      final int charIndex = random.nextInt(RECOVERY_PASSWORD_CHARACTERS.length());
      password.append(RECOVERY_PASSWORD_CHARACTERS.charAt(charIndex));
    }

    return password.toString();
  }
}

