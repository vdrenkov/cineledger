package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps role domain models to DTO representations used by the API.
 */
public final class RoleMapper {
    private static final Logger log = LoggerFactory.getLogger(RoleMapper.class);

    private RoleMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps role values to role dto values.
     *
     * @param role
     *     role entity to transform
     * @return role dto result
     */
    public static RoleDto mapRoleToRoleDto(Role role) {
        log.info("Mapping the role {} to a role DTO", role.getName());
        return new RoleDto(role.getId(), role.getName());
    }

    /**
     * Maps roles values to role dtos values.
     *
     * @param roles
     *     role entities to transform
     * @return matching role dto values
     */
    public static List<RoleDto> mapRolesToRoleDtos(List<Role> roles) {
        return roles.stream().map(RoleMapper::mapRoleToRoleDto).toList();
    }
}

