package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dev.vdrenkov.cineledger.exceptions.RoleAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.RoleNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.requests.RoleRequest;
import dev.vdrenkov.cineledger.repositories.RoleRepository;
import dev.vdrenkov.cineledger.testutils.constants.RoleConstants;
import dev.vdrenkov.cineledger.testutils.factories.RoleFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock private RoleRepository roleRepository;

    @InjectMocks private RoleService roleService;

    @Test
    void testAddRole_noExceptions_success() {
        final Role expected = RoleFactory.getDefaultRole();

        when(roleRepository.save(any())).thenReturn(expected);

        final Role role = roleService.addRole(RoleFactory.getDefaultRoleRequest());

        assertEquals(expected, role);
    }

    @Test
    void testGetAllRoles_rolesFound_success() {
        final List<Role> expected = RoleFactory.getDefaultRoleList();

        when(roleRepository.findAll()).thenReturn(expected);

        final List<Role> roles = roleService.getAllRoles();

        assertEquals(expected, roles);
    }

    @Test
    void testGetAllRolesDto_rolesFound_success() {
        final List<RoleDto> expected = RoleFactory.getDefaultRoleDtoList();

        when(roleRepository.findAll()).thenReturn(RoleFactory.getDefaultRoleList());

        final List<RoleDto> roles = roleService.getAllRolesDto();

        assertEquals(expected, roles);
    }

    @Test
    void testGetRoleById_roleFound_success() {
        final Role expected = RoleFactory.getDefaultRole();

        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Role role = roleService.getRoleById(RoleConstants.ID);

        assertEquals(expected, role);
    }

    @Test
    void testGetRoleById_roleNotFound_throwsRoleNotFoundException() {
        assertThrows(RoleNotFoundException.class, () -> roleService.getRoleById(RoleConstants.ID));
    }

    @Test
    void testGetRoleDtoById_roleDtoFound_success() {
        final RoleDto expected = RoleFactory.getDefaultRoleDto();

        when(roleRepository.findById(anyInt()))
                .thenReturn(Optional.of(RoleFactory.getDefaultRole()));

        final RoleDto role = roleService.getRoleDtoById(RoleConstants.ID);

        assertEquals(expected, role);
    }

    @Test
    void testGetRoleByName_roleFound_success() {
        final Role expected = RoleFactory.getDefaultRole();

        when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.of(expected));

        final Role role = roleService.getRoleByName(RoleConstants.NAME);

        assertEquals(expected, role);
    }

    @Test
    void testGetRoleByName_roleNotFound_throwRoleNotFoundException() {
        when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.empty());

        assertThrows(
                RoleNotFoundException.class, () -> roleService.getRoleByName(RoleConstants.NAME));
    }

    @Test
    void testGetRoleDtoByName_roleDtoFound_success() {
        final RoleDto expected = RoleFactory.getDefaultRoleDto();

        when(roleRepository.findRoleByName(anyString()))
                .thenReturn(Optional.of(RoleFactory.getDefaultRole()));

        final RoleDto role = roleService.getRoleDtoByName(RoleConstants.NAME);

        assertEquals(expected, role);
    }

    @Test
    void testUpdateRole_roleUpdated_success() {
        final RoleDto expected = RoleFactory.getDefaultRoleDto();

        when(roleRepository.findById(anyInt()))
                .thenReturn(Optional.of(RoleFactory.getDefaultRole()));
        when(roleRepository.save(any())).thenReturn(RoleFactory.getDefaultRole());

        final RoleDto role =
                roleService.updateRole(RoleFactory.getDefaultRoleRequest(), RoleConstants.ID);

        assertEquals(expected, role);
    }

    @Test
    void testAddRole_roleExists_throwsRoleAlreadyExistsException() {
        when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.of(new Role()));

        final RoleRequest roleRequest = RoleFactory.getDefaultRoleRequest();
        assertThrows(RoleAlreadyExistsException.class, () -> roleService.addRole(roleRequest));
    }

    @Test
    void testDeleteRole_roleDeleted_success() {
        final RoleDto expected = RoleFactory.getDefaultRoleDto();

        when(roleRepository.findById(anyInt()))
                .thenReturn(Optional.of(RoleFactory.getDefaultRole()));

        final RoleDto role = roleService.deleteRole(RoleConstants.ID);

        assertEquals(expected, role);
    }
}
