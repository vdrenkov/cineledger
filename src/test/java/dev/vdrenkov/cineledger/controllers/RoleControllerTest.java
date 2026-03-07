package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.RoleService;
import dev.vdrenkov.cineledger.testutils.factories.RoleFactory;
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

import static dev.vdrenkov.cineledger.testutils.constants.RoleConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.RoleConstants.NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests role controller behavior.
 */
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    private static final String RETURN_OLD = "returnOld";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    /**
     * Verifies that add Role role Added success.
     */
    @Test
    void testAddRole_roleAdded_success() throws Exception {
        final String json = objectMapper.writeValueAsString(RoleFactory.getDefaultRoleRequest());
        when(roleService.addRole(any())).thenReturn(RoleFactory.getDefaultRole());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.ROLES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.ROLES_PATH + "/" + ID));
    }

    /**
     * Verifies that get All Roles no Exceptions success.
     */
    @Test
    void testGetAllRoles_noExceptions_success() throws Exception {
        when(roleService.getAllRolesDto()).thenReturn(RoleFactory.getDefaultRoleDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.ROLES_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].name").value(NAME));
    }

    /**
     * Verifies that get Role By Name role Found success.
     */
    @Test
    void testGetRoleByName_roleFound_success() throws Exception {
        when(roleService.getRoleDtoByName(anyString())).thenReturn(RoleFactory.getDefaultRoleDto());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.ROLES_PATH).queryParam("name", NAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.name").value(NAME));
    }

    /**
     * Verifies that update Role return Old True success.
     */
    @Test
    void testUpdateRole_returnOldTrue_success() throws Exception {
        when(roleService.updateRole(any(), anyInt())).thenReturn(RoleFactory.getDefaultRoleDto());
        final String json = objectMapper.writeValueAsString(RoleFactory.getDefaultRoleRequest());

        mockMvc
            .perform(put(URIConstants.ROLES_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.name").value(NAME));
    }

    /**
     * Verifies that update Role return Old False success.
     */
    @Test
    void testUpdateRole_returnOldFalse_success() throws Exception {
        when(roleService.updateRole(any(), anyInt())).thenReturn(RoleFactory.getDefaultRoleDto());
        final String json = objectMapper.writeValueAsString(RoleFactory.getDefaultRoleRequest());

        mockMvc
            .perform(put(URIConstants.ROLES_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete Role return Old True success.
     */
    @Test
    void testDeleteRole_returnOldTrue_success() throws Exception {
        when(roleService.deleteRole(anyInt())).thenReturn(RoleFactory.getDefaultRoleDto());

        mockMvc
            .perform(delete(URIConstants.ROLES_ID_PATH, ID).queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.name").value(NAME));
    }

    /**
     * Verifies that delete Role return Old False success.
     */
    @Test
    void testDeleteRole_returnOldFalse_success() throws Exception {
        when(roleService.deleteRole(anyInt())).thenReturn(RoleFactory.getDefaultRoleDto());

        mockMvc.perform(delete(URIConstants.ROLES_ID_PATH, ID)).andExpect(status().isNoContent());
    }
}




