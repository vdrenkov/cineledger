package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;

import java.util.List;

public final class RoleMapper {

    private RoleMapper() {

    }

    public static RoleDto mapRoleToRoleDto(Role role) {

        return new RoleDto(role.getId(), role.getName());
    }

    public static List<RoleDto> mapRolesToRoleDtos(List<Role> roles) {
        return roles.stream().map(RoleMapper::mapRoleToRoleDto).toList();
    }
}
