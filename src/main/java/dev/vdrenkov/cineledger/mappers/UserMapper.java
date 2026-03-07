package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps user domain models to DTO representations used by the API.
 */
@Component
public class UserMapper {
    private static final Logger log = LoggerFactory.getLogger(UserMapper.class);

    private final RoleMapper roleMapper;

    /**
     * Creates a new user mapper with its required collaborators.
     *
     * @param roleMapper
     *     role mapper used by the operation
     */
    @Autowired
    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**
     * Maps user values to user dto values.
     *
     * @param user
     *     user entity to transform
     * @return user dto result
     */
    public UserDto mapUserToUserDto(User user) {
        log.info(String.format("The user with username %s is being mapped to a user DTO", user.getUsername()));
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(),
            user.getJoinDate(), roleMapper.mapRolesToRoleDtos(user.getRoles()));
    }

    /**
     * Maps users values to user dtos values.
     *
     * @param users
     *     user entities to transform
     * @return matching user dto values
     */
    public List<UserDto> mapUsersToUserDtos(List<User> users) {
        return users.stream().map(this::mapUserToUserDto).collect(Collectors.toList());
    }
}

