package bg.vdrenkov.cineledger.controllers;

import bg.vdrenkov.cineledger.services.UserService;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
import tools.jackson.databind.ObjectMapper;
import bg.vdrenkov.cineledger.testUtils.constants.RoleConstants;
import bg.vdrenkov.cineledger.testUtils.constants.UserConstants;
import bg.vdrenkov.cineledger.testUtils.factories.HttpCookieFactory;
import bg.vdrenkov.cineledger.testUtils.factories.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static bg.vdrenkov.cineledger.testUtils.constants.HttpCookieConstants.COOKIE_NAME;
import static bg.vdrenkov.cineledger.testUtils.constants.HttpCookieConstants.COOKIE_VALUE;
import static bg.vdrenkov.cineledger.testUtils.constants.RoleConstants.NAME;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.EMAIL;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.FIRST_NAME;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.JOIN_DATE;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.LAST_NAME;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.USERNAME;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  public static final String COOKIE = COOKIE_NAME + "=" + COOKIE_VALUE;
  public static final String RETURN_OLD = "returnOld";
  private static final String SET_COOKIE = "Set-Cookie";
  private static final String JOIN_DATE_STRING = "joinDate";
  private static final String IS_BEFORE = "isBefore";
  private static final String RECOVER_PASSWORD_PATH = "/password-recovery";

  private final static ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(userController)
      .build();
  }

  @Test
  public void testLogin_cookieObtained_success() throws Exception {
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());
    when(userService.login(any())).thenReturn(HttpCookieFactory.getDefaultHttpCookie());

    mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.LOGIN_PATH)
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(json))
           .andExpect(status().isOk())
           .andExpect(header().string(SET_COOKIE, COOKIE));
  }

  @Test
  public void testRegisterUser_cookieObtained_success() throws Exception {
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());
    when(userService.registerUser(any())).thenReturn(HttpCookieFactory.getDefaultHttpCookie());

    mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.REGISTRATION_PATH)
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string(SET_COOKIE, COOKIE));
  }

  @Test
  public void testRegisterUserByAdmin_createdWithoutCookie_success() throws Exception {
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultAdminRequest());

    mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.ADMINS_PATH)
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().doesNotExist(SET_COOKIE));
  }

  @Test
  public void testGetUserByUsername_userFound_success() throws Exception {
    when(userService.getUserDtoByUsername(anyString())).thenReturn(UserFactory.getDefaultUserDto());

    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.USERS_PATH)
                                          .queryParam("username", USERNAME))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate").value(JOIN_DATE.toString()))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testGetUserByEmail_userFound_success() throws Exception {
    when(userService.getUserDtoByEmail(anyString())).thenReturn(UserFactory.getDefaultUserDto());

    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.USERS_PATH)
                                          .queryParam("email", EMAIL))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(UserConstants.ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate").value(JOIN_DATE.toString()))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testGetUserByRoleName_userFound_success() throws Exception {
    when(userService.getUsersDtoByRoleName(anyString())).thenReturn(UserFactory.getDefaultUserDtoList());

    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.USERS_PATH)
                                          .queryParam("roleName", NAME))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].username").value(USERNAME))
           .andExpect(jsonPath("$[0].email").value(EMAIL))
           .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$[0].lastName").value(LAST_NAME))
           .andExpect(jsonPath("$[0].joinDate").value(JOIN_DATE.toString()))
           .andExpect(jsonPath("$[0].roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$[0].roles[0].name").value(NAME));
  }

  @Test
  public void testGetUsersByJoinDate_returnsUsersList_success() throws Exception {
    when(userService.getUsersDtosByJoinDate(any(), anyBoolean())).thenReturn(UserFactory.getDefaultUserDtoList());

    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.USERS_PATH)
                                          .queryParam(JOIN_DATE_STRING, String.valueOf(JOIN_DATE))
                                          .queryParam(IS_BEFORE, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].username").value(USERNAME))
           .andExpect(jsonPath("$[0].email").value(EMAIL))
           .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$[0].lastName").value(LAST_NAME))
           .andExpect(jsonPath("$[0].joinDate").value(JOIN_DATE.toString()))
           .andExpect(jsonPath("$[0].roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$[0].roles[0].name").value(NAME));
  }

  @Test
  public void testUpdateUser_returnOldTrue_success() throws Exception {
    when(userService.updateUser(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());

    mockMvc.perform(put(URIConstants.USERS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate").value(JOIN_DATE.toString()))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testUpdateUser_returnOldFalse_success() throws Exception {
    when(userService.updateUser(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());

    mockMvc.perform(put(URIConstants.USERS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testUpdateUserByAdmin_returnOldTrue_success() throws Exception {
    when(userService.updateUserByAdmin(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultAdminRequest());

    mockMvc.perform(put(URIConstants.ADMINS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate").value(JOIN_DATE.toString()))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testUpdateUserByAdmin_returnOldFalse_success() throws Exception {
    when(userService.updateUserByAdmin(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
    String json = objectMapper.writeValueAsString(UserFactory.getDefaultAdminRequest());

    mockMvc.perform(put(URIConstants.ADMINS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteUser_returnOldTrue_success() throws Exception {
    when(userService.deleteUser(anyInt())).thenReturn(UserFactory.getDefaultUserDto());

    mockMvc.perform(delete(URIConstants.USERS_ID_PATH, ID)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.username").value(USERNAME))
           .andExpect(jsonPath("$.email").value(EMAIL))
           .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.joinDate").value(JOIN_DATE.toString()))
           .andExpect(jsonPath("$.roles[0].id").value(RoleConstants.ID))
           .andExpect(jsonPath("$.roles[0].name").value(NAME));
  }

  @Test
  public void testDeleteUser_returnOldFalse_success() throws Exception {
    when(userService.deleteUser(anyInt())).thenReturn(UserFactory.getDefaultUserDto());

    mockMvc.perform(delete(URIConstants.USERS_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testRecoverPassword_returnsOk_success() throws Exception {
    when(userService.recoverPassword(anyString())).thenReturn(UserFactory.getDefaultUser());

    mockMvc.perform(patch(RECOVER_PASSWORD_PATH)
                      .queryParam("username", USERNAME))
           .andExpect(status().isOk());
  }
}



