package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps role domain models to DTO representations used by the API.
 */
@Component
public class RoleMapper {
    private static final Logger log = LoggerFactory.getLogger(RoleMapper.class);

    /**
     * Maps role values to role dto values.
     *
     * @param role
     *     role entity to transform
     * @return role dto result
     */
    public RoleDto mapRoleToRoleDto(Role role) {
        log.info(String.format("The role %s is being mapped to a role DTO", role.getName()));
        return new RoleDto(role.getId(), role.getName());
    }

    /**
     * Maps roles values to role dtos values.
     *
     * @param roles
     *     role entities to transform
     * @return matching role dto values
     */
    public List<RoleDto> mapRolesToRoleDtos(List<Role> roles) {
        return roles.stream().map(this::mapRoleToRoleDto).collect(Collectors.toList());
    }
}

