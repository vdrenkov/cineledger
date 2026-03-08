package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.testutils.constants.RoleConstants;
import dev.vdrenkov.cineledger.testutils.factories.RoleFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests role mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class RoleMapperTest {
    /**
     * Verifies that map Role To Role DTO success.
     */
    @Test
    void testMapRoleToRoleDto_success() {
        final RoleDto roleDto = RoleMapper.mapRoleToRoleDto(RoleFactory.getDefaultRole());

        assertEquals(RoleConstants.ID, roleDto.getId());
        assertEquals(RoleConstants.NAME, roleDto.getName());
    }

    /**
     * Verifies that map Roles To Role DTOs success.
     */
    @Test
    void testMapRolesToRoleDtos_success() {
        final List<RoleDto> result = RoleMapper.mapRolesToRoleDtos(RoleFactory.getDefaultRoleList());

        assertEquals(RoleConstants.ID, result.getFirst().getId());
        assertEquals(RoleConstants.NAME, result.getFirst().getName());
    }
}




