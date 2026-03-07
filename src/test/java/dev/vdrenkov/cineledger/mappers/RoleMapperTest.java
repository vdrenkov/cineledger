package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.testutils.constants.RoleConstants;
import dev.vdrenkov.cineledger.testutils.factories.RoleFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Tests role mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class RoleMapperTest {

    @InjectMocks
    private RoleMapper roleMapper;

    /**
     * Verifies that map Role To Role DTO success.
     */
    @Test
    void testMapRoleToRoleDto_success() {
        final RoleDto roleDto = roleMapper.mapRoleToRoleDto(RoleFactory.getDefaultRole());

        Assertions.assertEquals(RoleConstants.ID, roleDto.getId());
        Assertions.assertEquals(RoleConstants.NAME, roleDto.getName());
    }

    /**
     * Verifies that map Roles To Role DTOs success.
     */
    @Test
    void testMapRolesToRoleDtos_success() {
        final List<RoleDto> result = roleMapper.mapRolesToRoleDtos(RoleFactory.getDefaultRoleList());

        Assertions.assertEquals(RoleConstants.ID, result.get(0).getId());
        Assertions.assertEquals(RoleConstants.NAME, result.get(0).getName());
    }
}




