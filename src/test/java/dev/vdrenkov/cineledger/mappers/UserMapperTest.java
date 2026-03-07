package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.testUtils.constants.UserConstants;
import dev.vdrenkov.cineledger.testUtils.factories.RoleFactory;
import dev.vdrenkov.cineledger.testUtils.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserMapper userMapper;

    @Test
    public void testMapUsersToUserDtos_success() {
        List<User> users = UserFactory.getDefaultUserList();
        List<UserDto> expectedDtoList = UserFactory.getDefaultUserDtoList();
        when(roleMapper.mapRolesToRoleDtos(any())).thenReturn(RoleFactory.getDefaultRoleDtoList());

        List<UserDto> actualDtoList = userMapper.mapUsersToUserDtos(users);

        Assertions.assertEquals(expectedDtoList.size(), actualDtoList.size());

        for (int i = 0; i < expectedDtoList.size(); i++) {
            UserDto expectedDto = expectedDtoList.get(i);
            UserDto actualDto = actualDtoList.get(i);

            Assertions.assertEquals(expectedDto.getId(), actualDto.getId());
            Assertions.assertEquals(expectedDto.getUsername(), actualDto.getUsername());
            Assertions.assertEquals(expectedDto.getEmail(), actualDto.getEmail());
            Assertions.assertEquals(expectedDto.getFirstName(), actualDto.getFirstName());
            Assertions.assertEquals(expectedDto.getLastName(), actualDto.getLastName());
            Assertions.assertEquals(expectedDto.getJoinDate(), actualDto.getJoinDate());
            Assertions.assertEquals(expectedDto.getRoles().get(0).getId(), actualDto.getRoles().get(0).getId());
            Assertions.assertEquals(expectedDto.getRoles().get(0).getName(), actualDto.getRoles().get(0).getName());
        }
    }

    @Test
    public void testMapUserToUserDto_success() {
        when(roleMapper.mapRolesToRoleDtos(any())).thenReturn(RoleFactory.getDefaultRoleDtoList());
        User user = UserFactory.getDefaultUser();

        UserDto actualDto = userMapper.mapUserToUserDto(user);

        Assertions.assertEquals(UserConstants.USERNAME, actualDto.getUsername());
        Assertions.assertEquals(UserConstants.EMAIL, actualDto.getEmail());
        Assertions.assertEquals(UserConstants.FIRST_NAME, actualDto.getFirstName());
        Assertions.assertEquals(UserConstants.LAST_NAME, actualDto.getLastName());
        Assertions.assertEquals(UserConstants.JOIN_DATE, actualDto.getJoinDate());
        Assertions.assertEquals(RoleFactory.getDefaultRoleDtoList().get(0).getName(),
            actualDto.getRoles().get(0).getName());
    }
}




