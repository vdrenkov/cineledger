package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.RoleAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.RoleNotFoundException;
import dev.vdrenkov.cineledger.mappers.RoleMapper;
import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.repositories.RoleRepository;
import dev.vdrenkov.cineledger.testUtils.constants.RoleConstants;
import dev.vdrenkov.cineledger.testUtils.factories.RoleFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void testAddRole_noExceptions_success() {
        Role expected = RoleFactory.getDefaultRole();

        when(roleRepository.save(any())).thenReturn(expected);

        Role role = roleService.addRole(RoleFactory.getDefaultRoleRequest());

        assertEquals(expected, role);
    }

    @Test
    public void testGetAllRoles_rolesFound_success() {
        List<Role> expected = RoleFactory.getDefaultRoleList();

        when(roleRepository.findAll()).thenReturn(expected);

        List<Role> roles = roleService.getAllRoles();

        assertEquals(expected, roles);
    }

    @Test
    public void testGetAllRolesDto_rolesFound_success() {
        List<RoleDto> expected = RoleFactory.getDefaultRoleDtoList();

        when(roleRepository.findAll()).thenReturn(RoleFactory.getDefaultRoleList());
        when(roleMapper.mapRolesToRoleDtos(any())).thenReturn(expected);

        List<RoleDto> roles = roleService.getAllRolesDto();

        assertEquals(expected, roles);
    }

    @Test
    public void testGetRoleById_roleFound_success() {
        Role expected = RoleFactory.getDefaultRole();

        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Role role = roleService.getRoleById(RoleConstants.ID);

        assertEquals(expected, role);
    }

    @Test
    public void testGetRoleById_roleNotFound_throwsRoleNotFoundException() {
        assertThrows(RoleNotFoundException.class, () -> {

            roleService.getRoleById(RoleConstants.ID);

        });
    }

    @Test
    public void testGetRoleDtoById_roleDtoFound_success() {
        RoleDto expected = RoleFactory.getDefaultRoleDto();

        when(roleMapper.mapRoleToRoleDto(any())).thenReturn(expected);
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(RoleFactory.getDefaultRole()));

        RoleDto role = roleService.getRoleDtoById(RoleConstants.ID);

        assertEquals(expected, role);
    }

    @Test
    public void testGetRoleByName_roleFound_success() {
        Role expected = RoleFactory.getDefaultRole();

        when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.of(expected));

        Role role = roleService.getRoleByName(RoleConstants.NAME);

        assertEquals(expected, role);
    }

    @Test
    public void testGetRoleByName_roleNotFound_throwRoleNotFoundException() {
        assertThrows(RoleNotFoundException.class, () -> {

            when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.empty());

            roleService.getRoleByName(RoleConstants.NAME);

        });
    }

    @Test
    public void testGetRoleDtoByName_roleDtoFound_success() {
        RoleDto expected = RoleFactory.getDefaultRoleDto();

        when(roleMapper.mapRoleToRoleDto(any())).thenReturn(expected);
        when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.of(RoleFactory.getDefaultRole()));

        RoleDto role = roleService.getRoleDtoByName(RoleConstants.NAME);

        assertEquals(expected, role);
    }

    @Test
    public void testUpdateRole_roleUpdated_success() {
        RoleDto expected = RoleFactory.getDefaultRoleDto();

        when(roleMapper.mapRoleToRoleDto(any())).thenReturn(expected);
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(new Role()));
        when(roleRepository.save(any())).thenReturn(RoleFactory.getDefaultRole());

        RoleDto role = roleService.updateRole(RoleFactory.getDefaultRoleRequest(), RoleConstants.ID);

        assertEquals(expected, role);
    }

    @Test
    public void testAddRole_roleExists_throwsRoleAlreadyExistsException() {
        assertThrows(RoleAlreadyExistsException.class, () -> {

            RoleDto expected = RoleFactory.getDefaultRoleDto();

            when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.of(new Role()));

            roleService.addRole(RoleFactory.getDefaultRoleRequest());

        });
    }

    @Test
    public void testDeleteRole_roleDeleted_success() {
        RoleDto expected = RoleFactory.getDefaultRoleDto();

        when(roleMapper.mapRoleToRoleDto(any())).thenReturn(expected);
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(RoleFactory.getDefaultRole()));

        RoleDto role = roleService.deleteRole(RoleConstants.ID);

        assertEquals(expected, role);
    }
}



