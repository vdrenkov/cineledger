package bg.vdrenkov.cineledger.mappers;

import bg.vdrenkov.cineledger.models.dtos.RoleDto;
import bg.vdrenkov.cineledger.testUtils.constants.RoleConstants;
import bg.vdrenkov.cineledger.testUtils.factories.RoleFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RoleMapperTest {

    @InjectMocks
    private RoleMapper roleMapper;

    @Test
    public void testMapRoleToRoleDto_success() {
        RoleDto roleDto = roleMapper.mapRoleToRoleDto(RoleFactory.getDefaultRole());

        Assertions.assertEquals(RoleConstants.ID, roleDto.getId());
        Assertions.assertEquals(RoleConstants.NAME, roleDto.getName());
    }

    @Test
    public void testMapRolesToRoleDtos_success() {
        List<RoleDto> result = roleMapper.mapRolesToRoleDtos(RoleFactory.getDefaultRoleList());

        Assertions.assertEquals(RoleConstants.ID, result.get(0).getId());
        Assertions.assertEquals(RoleConstants.NAME, result.get(0).getName());
    }
}




