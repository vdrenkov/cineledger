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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Test
    void testMapUsersToUserDtos_success() {
        final List<User> users = UserFactory.getDefaultUserList();
        final List<UserDto> expectedDtoList = UserFactory.getDefaultUserDtoList();

        final List<UserDto> actualDtoList = UserMapper.mapUsersToUserDtos(users);

        assertEquals(expectedDtoList.size(), actualDtoList.size());

        for (int i = 0; i < expectedDtoList.size(); i++) {
            final UserDto expectedDto = expectedDtoList.get(i);
            final UserDto actualDto = actualDtoList.get(i);

            assertEquals(expectedDto.id(), actualDto.id());
            assertEquals(expectedDto.username(), actualDto.username());
            assertEquals(expectedDto.email(), actualDto.email());
            assertEquals(expectedDto.firstName(), actualDto.firstName());
            assertEquals(expectedDto.lastName(), actualDto.lastName());
            assertEquals(expectedDto.joinDate(), actualDto.joinDate());
            assertEquals(expectedDto.roles().getFirst().id(), actualDto.roles().getFirst().id());
            assertEquals(expectedDto.roles().getFirst().name(), actualDto.roles().getFirst().name());
        }
    }

    @Test
    void testMapUserToUserDto_success() {
        final User user = UserFactory.getDefaultUser();

        final UserDto actualDto = UserMapper.mapUserToUserDto(user);

        assertEquals(UserConstants.USERNAME, actualDto.username());
        assertEquals(UserConstants.EMAIL, actualDto.email());
        assertEquals(UserConstants.FIRST_NAME, actualDto.firstName());
        assertEquals(UserConstants.LAST_NAME, actualDto.lastName());
        assertEquals(UserConstants.JOIN_DATE, actualDto.joinDate());
        assertEquals(RoleFactory.getDefaultRoleDtoList().getFirst().name(),
            actualDto.roles().getFirst().name());
    }
}
