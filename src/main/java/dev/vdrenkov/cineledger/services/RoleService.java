package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.RoleAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.RoleNotFoundException;
import dev.vdrenkov.cineledger.mappers.RoleMapper;
import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.requests.RoleRequest;
import dev.vdrenkov.cineledger.repositories.RoleRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import dev.vdrenkov.cineledger.utils.constants.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains business logic for role operations.
 */
@Service
public class RoleService {
    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    /**
     * Creates a new role service with its required collaborators.
     *
     * @param roleRepository
     *     role repository used by the operation
     */
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Creates and persists role.
     *
     * @param roleRequest
     *     request payload for the role operation
     * @return requested role value
     */
    public Role addRole(RoleRequest roleRequest) {
        log.info("Trying to add a new role");

        roleValidation(roleRequest);

        return roleRepository.save(new Role(roleRequest.getName()));
    }

    /**
     * Returns all roles matching the supplied criteria.
     *
     * @return matching role values
     */
    public List<Role> getAllRoles() {
        log.info("Trying to retrieve all roles");

        return roleRepository.findAll();
    }

    /**
     * Returns all roles matching the supplied criteria.
     *
     * @return matching role dto values
     */
    public List<RoleDto> getAllRolesDto() {
        log.info("Trying to retrieve all roles DTOs");

        return RoleMapper.mapRolesToRoleDtos(getAllRoles());
    }

    /**
     * Returns role matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested role value
     */
    public Role getRoleById(int id) {
        log.info("Trying to retrieve role with id {}", id);

        return roleRepository.findById(id).orElseThrow(() -> {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.ROLE_NOT_FOUND_MESSAGE);

            return new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns role matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return role dto result
     */
    public RoleDto getRoleDtoById(int id) {
        log.info("Trying to retrieve role DTO with id {}", id);

        return RoleMapper.mapRoleToRoleDto(getRoleById(id));
    }

    /**
     * Returns role matching the supplied criteria.
     *
     * @param name
     *     name used by the operation
     * @return requested role value
     */
    public Role getRoleByName(String name) {
        log.info("Trying to retrieve role with name {}", name);

        return roleRepository.findRoleByName(name.toUpperCase()).orElseThrow(() -> {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.ROLE_NOT_FOUND_MESSAGE);

            return new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns role matching the supplied criteria.
     *
     * @param name
     *     name used by the operation
     * @return role dto result
     */
    public RoleDto getRoleDtoByName(String name) {
        log.info("Trying to retrieve role DTO with name {}", name);

        return RoleMapper.mapRoleToRoleDto(getRoleByName(name));
    }

    /**
     * Updates role and returns the previous state when needed.
     *
     * @param roleRequest
     *     request payload for the role operation
     * @param id
     *     identifier of the target resource
     * @return role dto result
     */
    public RoleDto updateRole(RoleRequest roleRequest, int id) {
        final RoleDto roleDto = getRoleDtoById(id);

        roleRepository.save(new Role(id, roleRequest.getName()));

        log.info("Role with id {} was updated", id);

        return roleDto;
    }

    /**
     * Deletes role and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @return role dto result
     */
    public RoleDto deleteRole(int id) {
        final RoleDto roleDto = getRoleDtoById(id);

        roleRepository.deleteById(id);

        log.info("Role with id {} was deleted", id);

        return roleDto;
    }

    private void roleValidation(RoleRequest roleRequest) {
        roleRepository.findRoleByName(roleRequest.getName()).ifPresent(role -> {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.ROLE_ALREADY_EXISTS_MESSAGE);

            throw new RoleAlreadyExistsException(ExceptionMessages.ROLE_ALREADY_EXISTS_MESSAGE);
        });
    }
}

