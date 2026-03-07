package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import bg.vdrenkov.cineledger.exceptions.NotLoggedInException;
import bg.vdrenkov.cineledger.exceptions.RoleNotChosenException;
import bg.vdrenkov.cineledger.exceptions.UserEmailAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.UserNotFoundException;
import bg.vdrenkov.cineledger.exceptions.UsernameAlreadyExistsException;
import bg.vdrenkov.cineledger.jwt.JwtCookieUtil;
import bg.vdrenkov.cineledger.mappers.UserMapper;
import bg.vdrenkov.cineledger.models.entities.Role;
import bg.vdrenkov.cineledger.models.entities.User;
import bg.vdrenkov.cineledger.models.requests.LoginRequest;
import bg.vdrenkov.cineledger.repositories.UserRepository;
import bg.vdrenkov.cineledger.testUtils.constants.RoleConstants;
import bg.vdrenkov.cineledger.testUtils.constants.UserConstants;
import bg.vdrenkov.cineledger.testUtils.factories.JwtFactory;
import bg.vdrenkov.cineledger.testUtils.factories.RoleFactory;
import bg.vdrenkov.cineledger.testUtils.factories.UserFactory;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import bg.vdrenkov.cineledger.models.dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

  private static final int INVALID_ID = 2;
  private static final String SECRET = "test-secret-for-jwt-tests-should-be-at-least-thirty-two-characters";

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @Mock
  private EmailService emailService;

  @Mock
  private JwtCookieUtil jwtCookieUtil;

  @Mock
  private UserRepository userRepository;

  @Mock
  private Random random;

  @Mock
  private RoleService roleService;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  public void setUp() {
    JwtFactory.setSecret(SECRET);
  }

  @Test
  public void testLogin_cookieObtained_success() {
    UserDetails userDetails = mock(UserDetails.class);

    Authentication authentication = mock(Authentication.class);

    HttpCookie httpCookie = mock(HttpCookie.class);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(jwtCookieUtil.createJWTCookie(userDetails)).thenReturn(httpCookie);

    HttpCookie resultCookie = userService.login(new LoginRequest(UserConstants.USERNAME, UserConstants.PASSWORD));
    assertEquals(resultCookie, httpCookie);
  }

  @Test
  public void testRegisterUser_userRegistered_cookieObtained() throws MailjetSocketTimeoutException, MailjetException {
    HttpCookie httpCookie = JwtFactory.getDefaultHttpCookie();

    when(passwordEncoder.encode(anyString())).thenReturn(UserConstants.PASSWORD);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
    when(userRepository.save(any())).thenReturn(UserFactory.getDefaultUser());
    when(authenticationManager.authenticate(any())).thenReturn(JwtFactory.getDefaultAuthentication());
    when(jwtCookieUtil.createJWTCookie(any())).thenReturn(httpCookie);

    HttpCookie resultCookie = userService.registerUser(UserFactory.getDefaultUserRequest());

    assertEquals(resultCookie, httpCookie);
  }

  @Test
  public void testRegisterUserByAdmin_userRegistered_notLoggedIn() {
    when(passwordEncoder.encode(anyString())).thenReturn(UserConstants.PASSWORD);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
    when(userRepository.save(any())).thenReturn(UserFactory.getDefaultUser());

    userService.registerUserByAdmin(UserFactory.getDefaultAdminRequest());

    verify(authenticationManager, never()).authenticate(any());
    verify(jwtCookieUtil, never()).createJWTCookie(any());
  }

  @Test
  public void testAddUser_usernameExists_throwsUserEmailAlreadyExistsException() throws MailjetSocketTimeoutException,
    MailjetException {
    assertThrows(UsernameAlreadyExistsException.class, () -> {

      when(passwordEncoder.encode(anyString())).thenReturn(UserConstants.PASSWORD);
      when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(new User()));

      userService.addUser(UserFactory.getDefaultUserRequest());
    
    });
  }

  @Test
  public void testAddUser_emailExists_throwsUserEmailAlreadyExistsException() throws MailjetSocketTimeoutException,
    MailjetException {
    assertThrows(UserEmailAlreadyExistsException.class, () -> {

      when(passwordEncoder.encode(anyString())).thenReturn(UserConstants.PASSWORD);
      when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
      when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(new User()));

      userService.addUser(UserFactory.getDefaultUserRequest());
    
    });
  }

  @Test
  public void testGetRoleByName_success() {
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());

    Role result = userService.getRoleByName(RoleConstants.NAME);

    assertEquals(RoleFactory.getDefaultRole(), result);
  }

  @Test
  public void testGetDefaultRoleList_success() {
    List<Role> expected = RoleFactory.getDefaultRoleList();

    when(roleService.getRoleByName(RoleConstants.NAME)).thenReturn(RoleFactory.getDefaultRole());

    List<Role> roles = userService.getDefaultRoleList();

    assertEquals(expected, roles);
  }

  @Test
  public void testGetUserRoles_success() {
    Role role1 = new Role(1, "ADMIN");
    Role role2 = new Role(2, "USER");

    when(roleService.getRoleByName("ADMIN")).thenReturn(role1);
    when(roleService.getRoleByName("USER")).thenReturn(role2);

    List<String> roleNames = Arrays.asList("ADMIN", "USER");

    List<Role> roles = userService.getUserRoles(roleNames);

    assertEquals(2, roles.size());
    assertEquals(role1, roles.get(0));
    assertEquals(role2, roles.get(1));
  }

  @Test
  public void testGetUserRoles_rolesEmpty_throwsRoleNotChosenException() {
    assertThrows(RoleNotChosenException.class, () -> {

      userService.getUserRoles(Collections.emptyList());
    
    });
  }

  @Test
  public void testGetUserById_success() {
    User expected = UserFactory.getDefaultUser();

    when(userRepository.findById(anyInt())).thenReturn(Optional.of(expected));

    User user = userService.getUserById(UserConstants.ID);

    assertEquals(expected, user);
  }

  @Test
  public void testGetUserById_userNotFound() {
    when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserById(UserConstants.ID));
  }

  @Test
  public void testGetUserDtoById_success() {
    UserDto expected = UserFactory.getDefaultUserDto();

    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userMapper.mapUserToUserDto(UserFactory.getDefaultUser())).thenReturn(expected);

    UserDto userDto = userService.getUserDtoById(UserConstants.ID);

    assertEquals(expected, userDto);
  }

  @Test
  public void testGetUserByUsername_success() {
    User expected = UserFactory.getDefaultUser();

    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    User user = userService.getUserByUsername(UserConstants.USERNAME);

    assertEquals(expected, user);
  }

  @Test
  public void testGetUserByUsername_userNotFound() {
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(UserConstants.USERNAME));
  }

  @Test
  public void testGetUserDtoByUsername_success() {
    UserDto expected = UserFactory.getDefaultUserDto();

    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userMapper.mapUserToUserDto(UserFactory.getDefaultUser())).thenReturn(expected);

    UserDto user = userService.getUserDtoByUsername(UserConstants.USERNAME);

    assertEquals(expected, user);
  }

  @Test
  public void testGetUserByEmail_success() {
    User expected = UserFactory.getDefaultUser();

    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expected));

    User user = userService.getUserByEmail(UserConstants.EMAIL);

    assertEquals(expected, user);
  }

  @Test
  public void testGetUserByEmail_userNotFound() {
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(UserConstants.EMAIL));
  }

  @Test
  public void testGetUserDtoByEmail_success() {
    UserDto expected = UserFactory.getDefaultUserDto();

    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userMapper.mapUserToUserDto(UserFactory.getDefaultUser())).thenReturn(expected);

    UserDto result = userService.getUserDtoByEmail(UserConstants.EMAIL);

    assertEquals(expected, result);
  }

  @Test
  public void testGetUsersByRoleName_success() {
    List<User> expected = UserFactory.getDefaultUserList();

    when(userRepository.findAllByRolesName(RoleConstants.NAME)).thenReturn(expected);

    List<User> result = userService.getUsersByRoleName(RoleConstants.NAME);

    assertEquals(expected, result);
  }

  @Test
  public void testGetUsersDtoByRoleName_success() {
    List<UserDto> expected = UserFactory.getDefaultUserDtoList();

    when(userService.getUsersByRoleName(RoleConstants.NAME)).thenReturn(UserFactory.getDefaultUserList());
    when(userMapper.mapUsersToUserDtos(UserFactory.getDefaultUserList())).thenReturn(expected);

    List<UserDto> resultList = userService.getUsersDtoByRoleName(RoleConstants.NAME);

    assertEquals(expected, resultList);
  }

  @Test
  public void testGetUsersByJoinDate_before_success() {
    User user2 = UserFactory.getDefaultUser();
    user2.setId(2);
    List<User> expectedUsers = Arrays.asList(UserFactory.getDefaultUser(), user2);

    when(userRepository.findAllByJoinDateBefore(any(LocalDate.class))).thenReturn(expectedUsers);

    List<User> users = userService.getUsersByJoinDate(UserConstants.JOIN_DATE, true);

    assertEquals(expectedUsers, users);
  }

  @Test
  public void testGetUsersByJoinDate_after_success() {
    User user2 = UserFactory.getDefaultUser();
    user2.setId(2);
    List<User> expectedUsers = Arrays.asList(UserFactory.getDefaultUser(), user2);

    when(userRepository.findAllByJoinDateAfter(any(LocalDate.class))).thenReturn(expectedUsers);

    List<User> users = userService.getUsersByJoinDate(UserConstants.JOIN_DATE, false);

    assertEquals(expectedUsers, users);
  }

  @Test
  public void getUsersDtosByJoinDate() {
    User user2 = UserFactory.getDefaultUser();
    user2.setId(2);

    List<User> users = Arrays.asList(UserFactory.getDefaultUser(), user2);

    when(userService.getUsersByJoinDate(UserConstants.JOIN_DATE, true)).thenReturn(users);
    when(userService.getUsersByJoinDate(UserConstants.JOIN_DATE, true)).thenReturn(UserFactory.getDefaultUserList());
    when(userMapper.mapUsersToUserDtos(UserFactory.getDefaultUserList())).thenReturn(
      UserFactory.getDefaultUserDtoList());

    List<UserDto> actualUserDtos = userService.getUsersDtosByJoinDate(UserConstants.JOIN_DATE, true);
    assertEquals(UserFactory.getDefaultUserDtoList(), actualUserDtos);
  }

  @Test
  public void testUpdateUserByAdmin_userUpdated_success() {
    UserDto expected = UserFactory.getDefaultUserDto();

    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userMapper.mapUserToUserDto(any())).thenReturn(expected);
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());
    when(userRepository.save(any())).thenReturn(UserFactory.getDefaultUser());

    UserDto result = userService.updateUserByAdmin(UserFactory.getDefaultAdminRequest(), UserConstants.ID);

    assertEquals(expected, result);
  }

  @Test
  public void testUpdateUser_userUpdated_success() {
    User expectedUser = UserFactory.getDefaultUser();
    UserDto expectedUserDto = UserFactory.getDefaultUserDto();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Authentication authentication = JwtFactory.getDefaultAuthentication();
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userMapper.mapUserToUserDto(any())).thenReturn(expectedUserDto);
    when(passwordEncoder.encode(any())).thenReturn(UserConstants.PASSWORD);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expectedUser))
                                                        .thenReturn(Optional.empty());
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());

    UserDto resultUser = userService.updateUser(UserFactory.getDefaultUserRequest(), UserConstants.ID);

    assertEquals(expectedUserDto, resultUser);
  }

  @Test
  public void testUpdateUser_userNotAuthorized_throwsNotAuthorizedException() {
    assertThrows(NotAuthorizedException.class, () -> {

      SecurityContext securityContext = Mockito.mock(SecurityContext.class);
      SecurityContextHolder.setContext(securityContext);
      Authentication authentication = JwtFactory.getDefaultAuthentication();

      when(securityContext.getAuthentication()).thenReturn(authentication);
      when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
      when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
      when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultAdminRole());

      userService.updateUser(UserFactory.getDefaultUserRequest(), INVALID_ID);
    
    });
  }

  @Test
  public void testDeleteUser_userDeleted_success() {
    UserDto userDto = UserFactory.getDefaultUserDto();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());

    UserDto result = userService.deleteUser(UserConstants.ID);

    assertEquals(userDto, result);
  }

  @Test
  public void testDeleteUser_userNotAuthorized_throwsNotAuthorizedException() {
    assertThrows(NotAuthorizedException.class, () -> {

      UserDto userDto = UserFactory.getDefaultUserDto();
      SecurityContext securityContext = Mockito.mock(SecurityContext.class);
      SecurityContextHolder.setContext(securityContext);
      Authentication authentication = JwtFactory.getDefaultAuthentication();

      when(securityContext.getAuthentication()).thenReturn(authentication);
      when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
      when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
      when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
      when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultAdminRole());

      userService.deleteUser(INVALID_ID);
    
    });
  }

  @Test
  public void testGetCurrentUser_userLoggedIn_success() {
    User expected = UserFactory.getDefaultUser();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    User result = userService.getCurrentUser();

    assertEquals(expected, result);
  }

  @Test
  public void testGetUserByUsernameOnLogin_userNotFound_throwsUserNotFoundException() {
    assertThrows(UserNotFoundException.class, () -> {

      UserDto userDto = UserFactory.getDefaultUserDto();
      SecurityContext securityContext = Mockito.mock(SecurityContext.class);
      SecurityContextHolder.setContext(securityContext);
      Authentication authentication = JwtFactory.getDefaultAuthentication();

      when(securityContext.getAuthentication()).thenReturn(authentication);
      when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
      when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
      when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

      UserDto result = userService.deleteUser(UserConstants.ID);

      assertEquals(userDto, result);
    
    });
  }

  @Test
  public void testIsAuthorized_userNotLogged_throwsNotLoggedInException() {
    assertThrows(NotLoggedInException.class, () -> {

      UserDto userDto = UserFactory.getDefaultUserDto();
      SecurityContext securityContext = Mockito.mock(SecurityContext.class);
      SecurityContextHolder.setContext(securityContext);
      Authentication authentication = Mockito.mock(Authentication.class);

      when(authentication.getName()).thenReturn(null);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));

      UserDto result = userService.updateUser(UserFactory.getDefaultUserRequest(), UserConstants.ID);

      assertEquals(userDto, result);
    
    });
  }

  @Test
  public void testIsAuthorized_roleAdmin_returnsTrue() {
    User user = UserFactory.getDefaultUser();
    user.setRoles(Collections.singletonList(RoleFactory.getDefaultAdminRole()));
    UserDto userDto = UserFactory.getDefaultUserDto();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user)).thenReturn(Optional.empty());
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultAdminRole());

    UserDto result = userService.updateUser(UserFactory.getDefaultUserRequest(), UserConstants.ID);

    assertEquals(userDto, result);
  }

  @Test
  public void testIsCurrentUserAdmin_roleAdmin_returnsTrue() {
    User expected = UserFactory.getDefaultAdmin();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    boolean result = userService.isCurrentUserAdmin();

    assertTrue(result);
  }

  @Test
  public void testIsCurrentUserAuthorized_userIsAdmin_returnsTrue() {
    User expected = UserFactory.getDefaultAdmin();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    boolean result = userService.isCurrentUserAuthorized(UserConstants.ID);

    assertTrue(result);
  }

  @Test
  public void testIsCurrentUserAuthorized_userAuthorized_returnsTrue() {
    User expected = UserFactory.getDefaultUser();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    boolean result = userService.isCurrentUserAuthorized(UserConstants.ID);

    assertTrue(result);
  }

  @Test
  public void testRecoverPassword() throws MailjetSocketTimeoutException, MailjetException {
    User expectedUser = UserFactory.getDefaultUser();
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expectedUser));
    when(random.nextInt(anyInt())).thenReturn(1);
    when(passwordEncoder.encode(any())).thenReturn(UserConstants.PASSWORD);
    when(userRepository.save(any())).thenReturn(expectedUser);
    Mockito.doNothing().when(emailService).sendPasswordConfirmationEmail(any(), anyString());

    User resultUser = userService.recoverPassword(UserConstants.USERNAME);

    assertEquals(expectedUser, resultUser);
  }
}



