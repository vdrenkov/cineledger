package bg.vdrenkov.cineledger.mappers;

import bg.vdrenkov.cineledger.models.dtos.RoleDto;
import bg.vdrenkov.cineledger.models.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

  private static final Logger log = LoggerFactory.getLogger(RoleMapper.class);

  public RoleDto mapRoleToRoleDto(Role role) {
    log.info(String.format("The role %s is being mapped to a role DTO", role.getName()));
    return new RoleDto(role.getId(), role.getName());
  }

  public List<RoleDto> mapRolesToRoleDtos(List<Role> roles) {
    return roles.stream()
                .map(this::mapRoleToRoleDto)
                .collect(Collectors.toList());
  }
}

