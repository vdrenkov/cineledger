package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.User;

import java.util.List;

public final class UserMapper {

    private UserMapper() {}

    public static UserDto mapUserToUserDto(User user) {

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getJoinDate(),
                RoleMapper.mapRolesToRoleDtos(user.getRoles()));
    }

    public static List<UserDto> mapUsersToUserDtos(List<User> users) {
        return users.stream().map(UserMapper::mapUserToUserDto).toList();
    }
}
