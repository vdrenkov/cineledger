package dev.vdrenkov.cineledger.testutil.factories;

import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.models.requests.AdminRequest;
import dev.vdrenkov.cineledger.models.requests.UserRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutil.constants.UserConstants.EMAIL;
import static dev.vdrenkov.cineledger.testutil.constants.UserConstants.FIRST_NAME;
import static dev.vdrenkov.cineledger.testutil.constants.UserConstants.ID;
import static dev.vdrenkov.cineledger.testutil.constants.UserConstants.JOIN_DATE;
import static dev.vdrenkov.cineledger.testutil.constants.UserConstants.LAST_NAME;
import static dev.vdrenkov.cineledger.testutil.constants.UserConstants.PASSWORD;
import static dev.vdrenkov.cineledger.testutil.constants.UserConstants.USERNAME;

/**
 * Provides reusable user fixtures for tests.
 */
public final class UserFactory {

    private UserFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default admin request fixture used in tests.
     *
     * @return test admin request value
     */
    public static AdminRequest getDefaultAdminRequest() {
        return new AdminRequest(USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME,
            RoleFactory.getDefaultRoleNamesList());
    }

    /**
     * Returns the default user request fixture used in tests.
     *
     * @return test user request value
     */
    public static UserRequest getDefaultUserRequest() {
        return new UserRequest(USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME);
    }

    /**
     * Returns the default user fixture used in tests.
     *
     * @return test user value
     */
    public static User getDefaultUser() {
        return new User(ID, USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, JOIN_DATE,
            RoleFactory.getDefaultRoleList());
    }

    /**
     * Returns the default admin fixture used in tests.
     *
     * @return test user value
     */
    public static User getDefaultAdmin() {
        return new User(ID, USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, JOIN_DATE,
            RoleFactory.getDefaultAdminRoleList());
    }

    /**
     * Returns the default user list fixture used in tests.
     *
     * @return test user values
     */
    public static List<User> getDefaultUserList() {
        return Collections.singletonList(getDefaultUser());
    }

    /**
     * Returns the default user dto fixture used in tests.
     *
     * @return test user dto value
     */
    public static UserDto getDefaultUserDto() {
        return new UserDto(ID, USERNAME, EMAIL, FIRST_NAME, LAST_NAME, JOIN_DATE, RoleFactory.getDefaultRoleDtoList());
    }

    /**
     * Returns the default user dto list fixture used in tests.
     *
     * @return test user dto values
     */
    public static List<UserDto> getDefaultUserDtoList() {
        return Collections.singletonList(getDefaultUserDto());
    }
}


