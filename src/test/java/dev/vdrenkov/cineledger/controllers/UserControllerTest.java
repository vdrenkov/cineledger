package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.UserService;
import dev.vdrenkov.cineledger.testutils.constants.RoleConstants;
import dev.vdrenkov.cineledger.testutils.constants.UserConstants;
import dev.vdrenkov.cineledger.testutils.factories.HttpCookieFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import static dev.vdrenkov.cineledger.testutils.constants.HttpCookieConstants.COOKIE_NAME;
import static dev.vdrenkov.cineledger.testutils.constants.HttpCookieConstants.COOKIE_VALUE;
import static dev.vdrenkov.cineledger.testutils.constants.RoleConstants.NAME;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.EMAIL;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.FIRST_NAME;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.JOIN_DATE;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.LAST_NAME;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests user controller behavior.
 */
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    /**
     * Provides the default cookie used in tests.
     */
    public static final String COOKIE = COOKIE_NAME + "=" + COOKIE_VALUE;
    /**
     * Provides the default return old used in tests.
     */
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

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    /**
     * Verifies that login cookie Obtained success.
     */
    @Test
    void testLogin_cookieObtained_success() throws Exception {
        final String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());
        when(userService.login(any())).thenReturn(HttpCookieFactory.getDefaultHttpCookie());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(header().string(SET_COOKIE, COOKIE));
    }

    /**
     * Verifies that register User cookie Obtained success.
     */
    @Test
    void testRegisterUser_cookieObtained_success() throws Exception {
        final String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());
        when(userService.registerUser(any())).thenReturn(HttpCookieFactory.getDefaultHttpCookie());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.REGISTRATION_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string(SET_COOKIE, COOKIE));
    }

    /**
     * Verifies that register User By Admin created Without Cookie success.
     */
    @Test
    void testRegisterUserByAdmin_createdWithoutCookie_success() throws Exception {
        final String json = objectMapper.writeValueAsString(UserFactory.getDefaultAdminRequest());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.ADMINS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().doesNotExist(SET_COOKIE));
    }

    /**
     * Verifies that get User By Username user Found success.
     */
    @Test
    void testGetUserByUsername_userFound_success() throws Exception {
        when(userService.getUserDtoByUsername(anyString())).thenReturn(UserFactory.getDefaultUserDto());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.USERS_PATH).queryParam("username", USERNAME))
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

    /**
     * Verifies that get User By Email user Found success.
     */
    @Test
    void testGetUserByEmail_userFound_success() throws Exception {
        when(userService.getUserDtoByEmail(anyString())).thenReturn(UserFactory.getDefaultUserDto());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.USERS_PATH).queryParam("email", EMAIL))
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

    /**
     * Verifies that get User By Role Name user Found success.
     */
    @Test
    void testGetUserByRoleName_userFound_success() throws Exception {
        when(userService.getUsersDtoByRoleName(anyString())).thenReturn(UserFactory.getDefaultUserDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.USERS_PATH).queryParam("roleName", NAME))
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

    /**
     * Verifies that get Users By Join Date returns Users List success.
     */
    @Test
    void testGetUsersByJoinDate_returnsUsersList_success() throws Exception {
        when(userService.getUsersDtosByJoinDate(any(), anyBoolean())).thenReturn(UserFactory.getDefaultUserDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders
                .get(URIConstants.USERS_PATH)
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

    /**
     * Verifies that update User return Old True success.
     */
    @Test
    void testUpdateUser_returnOldTrue_success() throws Exception {
        when(userService.updateUser(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
        final String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());

        mockMvc
            .perform(put(URIConstants.USERS_ID_PATH, ID)
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

    /**
     * Verifies that update User return Old False success.
     */
    @Test
    void testUpdateUser_returnOldFalse_success() throws Exception {
        when(userService.updateUser(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
        final String json = objectMapper.writeValueAsString(UserFactory.getDefaultUserRequest());

        mockMvc
            .perform(put(URIConstants.USERS_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that update User By Admin return Old True success.
     */
    @Test
    void testUpdateUserByAdmin_returnOldTrue_success() throws Exception {
        when(userService.updateUserByAdmin(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
        final String json = objectMapper.writeValueAsString(UserFactory.getDefaultAdminRequest());

        mockMvc
            .perform(put(URIConstants.ADMINS_ID_PATH, ID)
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

    /**
     * Verifies that update User By Admin return Old False success.
     */
    @Test
    void testUpdateUserByAdmin_returnOldFalse_success() throws Exception {
        when(userService.updateUserByAdmin(any(), anyInt())).thenReturn(UserFactory.getDefaultUserDto());
        final String json = objectMapper.writeValueAsString(UserFactory.getDefaultAdminRequest());

        mockMvc
            .perform(put(URIConstants.ADMINS_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete User return Old True success.
     */
    @Test
    void testDeleteUser_returnOldTrue_success() throws Exception {
        when(userService.deleteUser(anyInt())).thenReturn(UserFactory.getDefaultUserDto());

        mockMvc
            .perform(delete(URIConstants.USERS_ID_PATH, ID).queryParam(RETURN_OLD, String.valueOf(true)))
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

    /**
     * Verifies that delete User return Old False success.
     */
    @Test
    void testDeleteUser_returnOldFalse_success() throws Exception {
        when(userService.deleteUser(anyInt())).thenReturn(UserFactory.getDefaultUserDto());

        mockMvc.perform(delete(URIConstants.USERS_ID_PATH, ID)).andExpect(status().isNoContent());
    }

    /**
     * Verifies that recover Password returns Ok success.
     */
    @Test
    void testRecoverPassword_returnsOk_success() throws Exception {
        when(userService.recoverPassword(anyString())).thenReturn(UserFactory.getDefaultUser());

        mockMvc.perform(patch(RECOVER_PASSWORD_PATH).queryParam("username", USERNAME)).andExpect(status().isOk());
    }
}



