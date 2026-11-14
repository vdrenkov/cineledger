package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import dev.vdrenkov.cineledger.exceptions.NotLoggedInException;
import dev.vdrenkov.cineledger.exceptions.RoleNotChosenException;
import dev.vdrenkov.cineledger.exceptions.UserEmailAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.UserNotFoundException;
import dev.vdrenkov.cineledger.exceptions.UsernameAlreadyExistsException;
import dev.vdrenkov.cineledger.jwt.JwtCookieUtil;
import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.models.requests.CreateUserRequest;
import dev.vdrenkov.cineledger.models.requests.LoginRequest;
import dev.vdrenkov.cineledger.models.requests.UserRequest;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import dev.vdrenkov.cineledger.testutils.constants.RoleConstants;
import dev.vdrenkov.cineledger.testutils.constants.UserConstants;
import dev.vdrenkov.cineledger.testutils.factories.JwtFactory;
import dev.vdrenkov.cineledger.testutils.factories.RoleFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final int INVALID_ID = 2;
    private static final String SECRET =
            "pT6wYk3nRb9mQf2sHx8vLd4cZa1uEg7jKr5qNs0xVc6tMp2hWy9bDf3rLu8nQk4sC";
    final UserRequest userRequest = UserFactory.getDefaultUserRequest();

    @Mock private AuthenticationManager authenticationManager;

    @Mock private BCryptPasswordEncoder passwordEncoder;

    @Mock private EmailService emailService;

    @Mock private JwtCookieUtil jwtCookieUtil;

    @Mock private UserRepository userRepository;

    @Mock private RoleService roleService;

    @InjectMocks private UserService userService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        JwtFactory.setSecret(SECRET);
    }

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testLogin_cookieObtained_success() {
        final UserDetails userDetails = mock(UserDetails.class);

        final Authentication authentication = mock(Authentication.class);

        final HttpCookie httpCookie = mock(HttpCookie.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtCookieUtil.createJWTCookie(userDetails)).thenReturn(httpCookie);

        final HttpCookie resultCookie =
                userService.login(new LoginRequest(UserConstants.USERNAME, UserConstants.PASSWORD));
        assertEquals(resultCookie, httpCookie);
    }

    @Test
    void createUser_customerRequest_preservesAuthentication() {
        when(userRepository.save(any())).thenReturn(UserFactory.getDefaultUser());
        when(roleService.getRoleByName("USER")).thenReturn(new Role("USER"));

        final User created = userService.createUser(createRequest());

        assertEquals(UserConstants.USERNAME, created.getUsername());
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtCookieUtil, never()).createJWTCookie(any());
    }

    @Test
    void createUser_adminRequest_preservesAuthentication() {
        SecurityContextHolder.getContext()
                .setAuthentication(
                        UsernamePasswordAuthenticationToken.authenticated(
                                "administrator",
                                null,
                                List.of(
                                        new org.springframework.security.core.authority
                                                .SimpleGrantedAuthority("ADMIN"))));
        when(userRepository.save(any())).thenReturn(UserFactory.getDefaultUser());
        when(roleService.getRoleByName("VENDOR")).thenReturn(new Role("VENDOR"));
        final var request = createRequest();

        userService.createUser(
                new CreateUserRequest(
                        request.username(),
                        request.password(),
                        request.email(),
                        request.firstName(),
                        request.lastName(),
                        List.of("VENDOR")));

        verify(authenticationManager, never()).authenticate(any());
        verify(jwtCookieUtil, never()).createJWTCookie(any());
    }

    @Test
    void testAddUser_usernameExists_throwsUserEmailAlreadyExistsException() {

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> userService.createUser(createRequest()));
    }

    @Test
    void testAddUser_emailExists_throwsUserEmailAlreadyExistsException() {

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(
                UserEmailAlreadyExistsException.class,
                () -> userService.createUser(createRequest()));
    }

    @Test
    void testGetRoleByName_success() {
        when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());

        final Role result = userService.getRoleByName(RoleConstants.NAME);

        assertEquals(RoleFactory.getDefaultRole(), result);
    }

    @Test
    void testGetDefaultRoleList_success() {
        final List<Role> expected = RoleFactory.getDefaultRoleList();

        when(roleService.getRoleByName(RoleConstants.NAME))
                .thenReturn(RoleFactory.getDefaultRole());

        final List<Role> roles = userService.getDefaultRoleList();

        assertEquals(expected, roles);
    }

    @Test
    void testGetUserRoles_success() {
        final Role role1 = new Role(1, "ADMIN");
        final Role role2 = new Role(2, "USER");

        when(roleService.getRoleByName("ADMIN")).thenReturn(role1);
        when(roleService.getRoleByName("USER")).thenReturn(role2);

        final List<String> roleNames = Arrays.asList("ADMIN", "USER");

        final List<Role> roles = userService.getUserRoles(roleNames);

        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    @Test
    void testGetUserRoles_rolesEmpty_throwsRoleNotChosenException() {
        final List<String> emptyList = Collections.emptyList();
        assertThrows(RoleNotChosenException.class, () -> userService.getUserRoles(emptyList));
    }

    @Test
    void testGetUserById_success() {
        final User expected = UserFactory.getDefaultUser();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final User user = userService.getUserById(UserConstants.ID);

        assertEquals(expected, user);
    }

    @Test
    void testGetUserById_userNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(UserConstants.ID));
    }

    @Test
    void testGetUserDtoById_success() {
        final UserDto expected = UserFactory.getDefaultUserDto();

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));

        final UserDto userDto = userService.getUserDtoById(UserConstants.ID);

        assertEquals(expected, userDto);
    }

    @Test
    void testGetUserByUsername_success() {
        final User expected = UserFactory.getDefaultUser();

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

        final User user = userService.getUserByUsername(UserConstants.USERNAME);

        assertEquals(expected, user);
    }

    @Test
    void testGetUserByUsername_userNotFound() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserByUsername(UserConstants.USERNAME));
    }

    @Test
    void testGetUserDtoByUsername_success() {
        final UserDto expected = UserFactory.getDefaultUserDto();

        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));

        final UserDto user = userService.getUserDtoByUsername(UserConstants.USERNAME);

        assertEquals(expected, user);
    }

    @Test
    void testGetUserByEmail_success() {
        final User expected = UserFactory.getDefaultUser();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expected));

        final User user = userService.getUserByEmail(UserConstants.EMAIL);

        assertEquals(expected, user);
    }

    @Test
    void testGetUserByEmail_userNotFound() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class, () -> userService.getUserByEmail(UserConstants.EMAIL));
    }

    @Test
    void testGetUserDtoByEmail_success() {
        final UserDto expected = UserFactory.getDefaultUserDto();

        when(userRepository.findUserByEmail(anyString()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));

        final UserDto result = userService.getUserDtoByEmail(UserConstants.EMAIL);

        assertEquals(expected, result);
    }

    @Test
    void testGetUsersByRoleName_success() {
        final List<User> expected = UserFactory.getDefaultUserList();

        when(userRepository.findAllByRolesName(RoleConstants.NAME)).thenReturn(expected);

        final List<User> result = userService.getUsersByRoleName(RoleConstants.NAME);

        assertEquals(expected, result);
    }

    @Test
    void testGetUsersDtoByRoleName_success() {
        final List<UserDto> expected = UserFactory.getDefaultUserDtoList();

        when(userRepository.findAllByRolesName(RoleConstants.NAME))
                .thenReturn(UserFactory.getDefaultUserList());

        final List<UserDto> resultList = userService.getUsersDtoByRoleName(RoleConstants.NAME);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetUsersByJoinDate_before_success() {
        final User user2 = UserFactory.getDefaultUser();
        user2.setId(2);
        final List<User> expectedUsers = Arrays.asList(UserFactory.getDefaultUser(), user2);

        when(userRepository.findAllByJoinDateBefore(any(LocalDate.class)))
                .thenReturn(expectedUsers);

        final List<User> users = userService.getUsersByJoinDate(UserConstants.JOIN_DATE, true);

        assertEquals(expectedUsers, users);
    }

    @Test
    void testGetUsersByJoinDate_after_success() {
        final User user2 = UserFactory.getDefaultUser();
        user2.setId(2);
        final List<User> expectedUsers = Arrays.asList(UserFactory.getDefaultUser(), user2);

        when(userRepository.findAllByJoinDateAfter(any(LocalDate.class))).thenReturn(expectedUsers);

        final List<User> users = userService.getUsersByJoinDate(UserConstants.JOIN_DATE, false);

        assertEquals(expectedUsers, users);
    }

    @Test
    void getUsersDtosByJoinDate() {
        when(userRepository.findAllByJoinDateBefore(UserConstants.JOIN_DATE))
                .thenReturn(UserFactory.getDefaultUserList());

        final List<UserDto> actualUserDtos =
                userService.getUsersDtosByJoinDate(UserConstants.JOIN_DATE, true);
        assertEquals(UserFactory.getDefaultUserDtoList(), actualUserDtos);
    }

    @Test
    void testUpdateUserByAdmin_userUpdated_success() {
        final UserDto expected = UserFactory.getDefaultUserDto();

        when(userRepository.findByIdForUpdate(anyInt()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));
        when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());
        when(userRepository.save(any())).thenReturn(UserFactory.getDefaultUser());

        final UserDto result =
                userService.updateUserByAdmin(
                        UserFactory.getDefaultAdminRequest(), UserConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    void testUpdateUser_userUpdated_success() {
        final User expectedUser = UserFactory.getDefaultUser();
        final UserDto expectedUserDto = UserFactory.getDefaultUserDto();
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        final Authentication authentication = JwtFactory.getDefaultAuthentication();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(passwordEncoder.encode(any())).thenReturn(UserConstants.PASSWORD);
        when(userRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.of(expectedUser));
        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Optional.of(expectedUser))
                .thenReturn(Optional.empty());

        final UserDto resultUser = userService.updateUser(userRequest, UserConstants.ID);

        assertEquals(expectedUserDto, resultUser);
    }

    @Test
    void testUpdateUser_userNotAuthorized_throwsNotAuthorizedException() {
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final Authentication authentication = JwtFactory.getDefaultAuthentication();

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));

        assertThrows(
                NotAuthorizedException.class,
                () -> userService.updateUser(userRequest, INVALID_ID));
    }

    @Test
    void testDeleteUser_userDeleted_success() {
        final UserDto userDto = UserFactory.getDefaultUserDto();
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final Authentication authentication = JwtFactory.getDefaultAuthentication();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));
        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));

        final UserDto result = userService.deleteUser(UserConstants.ID);

        assertEquals(userDto, result);
    }

    @Test
    void testDeleteUser_userNotAuthorized_throwsNotAuthorizedException() {
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final Authentication authentication = JwtFactory.getDefaultAuthentication();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));
        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));

        assertThrows(NotAuthorizedException.class, () -> userService.deleteUser(INVALID_ID));
    }

    @Test
    void testGetCurrentUser_userLoggedIn_success() {
        final User expected = UserFactory.getDefaultUser();
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final Authentication authentication = JwtFactory.getDefaultAuthentication();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

        final User result = userService.getCurrentUser();

        assertEquals(expected, result);
    }

    @Test
    void testGetUserByUsernameOnLogin_userNotFound_throwsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(UserConstants.ID));
    }

    @Test
    void testIsAuthorized_userNotLogged_throwsNotLoggedInException() {
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn(null);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        assertThrows(
                NotLoggedInException.class,
                () -> userService.updateUser(userRequest, UserConstants.ID));
    }

    @Test
    void testIsAuthorized_roleAdmin_returnsTrue() {
        final User user = UserFactory.getDefaultUser();
        user.setRoles(Collections.singletonList(RoleFactory.getDefaultAdminRole()));
        final UserDto userDto = UserFactory.getDefaultUserDto();
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final Authentication authentication = JwtFactory.getDefaultAuthentication();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByIdForUpdate(anyInt()))
                .thenReturn(Optional.of(UserFactory.getDefaultUser()));
        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Optional.of(user))
                .thenReturn(Optional.empty());

        final UserDto result = userService.updateUser(userRequest, UserConstants.ID);

        assertEquals(userDto, result);
    }

    @Test
    void testIsCurrentUserAdmin_roleAdmin_returnsTrue() {
        final User expected = UserFactory.getDefaultAdmin();
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final Authentication authentication = JwtFactory.getDefaultAuthentication();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

        final boolean result = userService.isCurrentUserAdmin();

        assertTrue(result);
    }

    @Test
    void testIsCurrentUserAuthorized_userIsAdmin_returnsTrue() {
        final User expected = UserFactory.getDefaultAdmin();
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final Authentication authentication = JwtFactory.getDefaultAuthentication();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

        final boolean result = userService.isCurrentUserAuthorized(UserConstants.ID);

        assertTrue(result);
    }

    @Test
    void testIsCurrentUserAuthorized_userAuthorized_returnsTrue() {
        final User expected = UserFactory.getDefaultUser();
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final Authentication authentication = JwtFactory.getDefaultAuthentication();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

        final boolean result = userService.isCurrentUserAuthorized(UserConstants.ID);

        assertTrue(result);
    }

    private CreateUserRequest createRequest() {
        return new CreateUserRequest(
                userRequest.username(),
                userRequest.password(),
                userRequest.email(),
                userRequest.firstName(),
                userRequest.lastName(),
                null);
    }
}
