package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.testutils.constants.UserConstants;
import dev.vdrenkov.cineledger.testutils.factories.RoleFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests user mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    /**
     * Verifies that map Users To User DTOs success.
     */
    @Test
    void testMapUsersToUserDtos_success() {
        final List<User> users = UserFactory.getDefaultUserList();
        final List<UserDto> expectedDtoList = UserFactory.getDefaultUserDtoList();

        final List<UserDto> actualDtoList = UserMapper.mapUsersToUserDtos(users);

        assertEquals(expectedDtoList.size(), actualDtoList.size());

        for (int i = 0; i < expectedDtoList.size(); i++) {
            final UserDto expectedDto = expectedDtoList.get(i);
            final UserDto actualDto = actualDtoList.get(i);

            assertEquals(expectedDto.getId(), actualDto.getId());
            assertEquals(expectedDto.getUsername(), actualDto.getUsername());
            assertEquals(expectedDto.getEmail(), actualDto.getEmail());
            assertEquals(expectedDto.getFirstName(), actualDto.getFirstName());
            assertEquals(expectedDto.getLastName(), actualDto.getLastName());
            assertEquals(expectedDto.getJoinDate(), actualDto.getJoinDate());
            assertEquals(expectedDto.getRoles().getFirst().getId(), actualDto.getRoles().getFirst().getId());
            assertEquals(expectedDto.getRoles().getFirst().getName(), actualDto.getRoles().getFirst().getName());
        }
    }

    /**
     * Verifies that map User To User DTO success.
     */
    @Test
    void testMapUserToUserDto_success() {
        final User user = UserFactory.getDefaultUser();

        final UserDto actualDto = UserMapper.mapUserToUserDto(user);

        assertEquals(UserConstants.USERNAME, actualDto.getUsername());
        assertEquals(UserConstants.EMAIL, actualDto.getEmail());
        assertEquals(UserConstants.FIRST_NAME, actualDto.getFirstName());
        assertEquals(UserConstants.LAST_NAME, actualDto.getLastName());
        assertEquals(UserConstants.JOIN_DATE, actualDto.getJoinDate());
        assertEquals(RoleFactory.getDefaultRoleDtoList().getFirst().getName(),
            actualDto.getRoles().getFirst().getName());
    }
}




