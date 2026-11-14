package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.RoleAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.RoleNotFoundException;
import dev.vdrenkov.cineledger.mappers.RoleMapper;
import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.requests.RoleRequest;
import dev.vdrenkov.cineledger.repositories.RoleRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role addRole(RoleRequest roleRequest) {

        roleValidation(roleRequest);

        return roleRepository.save(new Role(roleRequest.name()));
    }

    public List<Role> getAllRoles() {

        return roleRepository.findAll();
    }

    public List<RoleDto> getAllRolesDto() {

        return RoleMapper.mapRolesToRoleDtos(getAllRoles());
    }

    public Role getRoleById(int id) {

        return roleRepository
                .findById(id)
                .orElseThrow(() -> {
                    return new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND_MESSAGE);
                });
    }

    public RoleDto getRoleDtoById(int id) {

        return RoleMapper.mapRoleToRoleDto(getRoleById(id));
    }

    public Role getRoleByName(String name) {

        return roleRepository
                .findRoleByName(name.toUpperCase())
                .orElseThrow(() -> {
                    return new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND_MESSAGE);
                });
    }

    public RoleDto getRoleDtoByName(String name) {

        return RoleMapper.mapRoleToRoleDto(getRoleByName(name));
    }

    public RoleDto updateRole(RoleRequest roleRequest, int id) {
        final RoleDto roleDto = getRoleDtoById(id);

        roleRepository.save(new Role(id, roleRequest.name()));

        return roleDto;
    }

    public RoleDto deleteRole(int id) {
        final RoleDto roleDto = getRoleDtoById(id);

        roleRepository.deleteById(id);

        return roleDto;
    }

    private void roleValidation(RoleRequest roleRequest) {
        roleRepository
                .findRoleByName(roleRequest.name())
                .ifPresent(role -> {
                    throw new RoleAlreadyExistsException(
                            ExceptionMessages.ROLE_ALREADY_EXISTS_MESSAGE);
                });
    }
}
