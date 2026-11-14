package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.testutils.constants.RoleConstants;
import dev.vdrenkov.cineledger.testutils.factories.RoleFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RoleMapperTest {

    @Test
    void testMapRoleToRoleDto_success() {
        final RoleDto roleDto = RoleMapper.mapRoleToRoleDto(RoleFactory.getDefaultRole());

        assertEquals(RoleConstants.ID, roleDto.id());
        assertEquals(RoleConstants.NAME, roleDto.name());
    }

    @Test
    void testMapRolesToRoleDtos_success() {
        final List<RoleDto> result = RoleMapper.mapRolesToRoleDtos(RoleFactory.getDefaultRoleList());

        assertEquals(RoleConstants.ID, result.getFirst().id());
        assertEquals(RoleConstants.NAME, result.getFirst().name());
    }
}
