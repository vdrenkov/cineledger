package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps user domain models to DTO representations used by the API.
 */
public final class UserMapper {
    private static final Logger log = LoggerFactory.getLogger(UserMapper.class);

    private UserMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps user values to user dto values.
     *
     * @param user
     *     user entity to transform
     * @return user dto result
     */
    public static UserDto mapUserToUserDto(User user) {
        log.info("Mapping the user with username {} to a user DTO", user.getUsername());
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(),
            user.getJoinDate(), RoleMapper.mapRolesToRoleDtos(user.getRoles()));
    }

    /**
     * Maps users values to user dtos values.
     *
     * @param users
     *     user entities to transform
     * @return matching user dto values
     */
    public static List<UserDto> mapUsersToUserDtos(List<User> users) {
        return users.stream().map(UserMapper::mapUserToUserDto).toList();
    }
}

