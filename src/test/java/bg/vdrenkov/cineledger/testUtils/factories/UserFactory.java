package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.dtos.UserDto;
import bg.vdrenkov.cineledger.models.entities.User;
import bg.vdrenkov.cineledger.models.requests.AdminRequest;
import bg.vdrenkov.cineledger.models.requests.UserRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.EMAIL;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.FIRST_NAME;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.JOIN_DATE;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.LAST_NAME;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.PASSWORD;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.USERNAME;

public final class UserFactory {

    private UserFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static AdminRequest getDefaultAdminRequest() {
        return new AdminRequest(USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME,
            RoleFactory.getDefaultRoleNamesList());
    }

    public static UserRequest getDefaultUserRequest() {
        return new UserRequest(USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME);
    }

    public static User getDefaultUser() {
        return new User(ID, USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, JOIN_DATE,
            RoleFactory.getDefaultRoleList());
    }

    public static User getDefaultAdmin() {
        return new User(ID, USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, JOIN_DATE,
            RoleFactory.getDefaultAdminRoleList());
    }

    public static List<User> getDefaultUserList() {
        return Collections.singletonList(getDefaultUser());
    }

    public static UserDto getDefaultUserDto() {
        return new UserDto(ID, USERNAME, EMAIL, FIRST_NAME, LAST_NAME, JOIN_DATE, RoleFactory.getDefaultRoleDtoList());
    }

    public static List<UserDto> getDefaultUserDtoList() {
        return Collections.singletonList(getDefaultUserDto());
    }
}


