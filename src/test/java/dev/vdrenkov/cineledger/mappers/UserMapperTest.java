package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.testutils.constants.UserConstants;
import dev.vdrenkov.cineledger.testutils.factories.RoleFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests user mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserMapper userMapper;

    /**
     * Verifies that map Users To User DTOs success.
     */
    @Test
    void testMapUsersToUserDtos_success() {
        final List<User> users = UserFactory.getDefaultUserList();
        final List<UserDto> expectedDtoList = UserFactory.getDefaultUserDtoList();
        when(roleMapper.mapRolesToRoleDtos(any())).thenReturn(RoleFactory.getDefaultRoleDtoList());

        final List<UserDto> actualDtoList = userMapper.mapUsersToUserDtos(users);

        Assertions.assertEquals(expectedDtoList.size(), actualDtoList.size());

        for (int i = 0; i < expectedDtoList.size(); i++) {
            final UserDto expectedDto = expectedDtoList.get(i);
            final UserDto actualDto = actualDtoList.get(i);

            Assertions.assertEquals(expectedDto.getId(), actualDto.getId());
            Assertions.assertEquals(expectedDto.getUsername(), actualDto.getUsername());
            Assertions.assertEquals(expectedDto.getEmail(), actualDto.getEmail());
            Assertions.assertEquals(expectedDto.getFirstName(), actualDto.getFirstName());
            Assertions.assertEquals(expectedDto.getLastName(), actualDto.getLastName());
            Assertions.assertEquals(expectedDto.getJoinDate(), actualDto.getJoinDate());
            Assertions.assertEquals(expectedDto.getRoles().getFirst().getId(), actualDto.getRoles().getFirst().getId());
            Assertions.assertEquals(expectedDto.getRoles().getFirst().getName(),
                actualDto.getRoles().getFirst().getName());
        }
    }

    /**
     * Verifies that map User To User DTO success.
     */
    @Test
    void testMapUserToUserDto_success() {
        when(roleMapper.mapRolesToRoleDtos(any())).thenReturn(RoleFactory.getDefaultRoleDtoList());
        final User user = UserFactory.getDefaultUser();

        final UserDto actualDto = userMapper.mapUserToUserDto(user);

        Assertions.assertEquals(UserConstants.USERNAME, actualDto.getUsername());
        Assertions.assertEquals(UserConstants.EMAIL, actualDto.getEmail());
        Assertions.assertEquals(UserConstants.FIRST_NAME, actualDto.getFirstName());
        Assertions.assertEquals(UserConstants.LAST_NAME, actualDto.getLastName());
        Assertions.assertEquals(UserConstants.JOIN_DATE, actualDto.getJoinDate());
        Assertions.assertEquals(RoleFactory.getDefaultRoleDtoList().getFirst().getName(),
            actualDto.getRoles().getFirst().getName());
    }
}




