package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.requests.RoleRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.RoleConstants.ADMIN;
import static dev.vdrenkov.cineledger.testutils.constants.RoleConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.RoleConstants.NAME;

/**
 * Provides reusable role fixtures for tests.
 */
public final class RoleFactory {

    private RoleFactory() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default role request fixture used in tests.
     *
     * @return test role request value
     */
    public static RoleRequest getDefaultRoleRequest() {
        return new RoleRequest(NAME);
    }

    /**
     * Returns the default role fixture used in tests.
     *
     * @return test role value
     */
    public static Role getDefaultRole() {
        return new Role(ID, NAME);
    }

    /**
     * Returns the default admin role fixture used in tests.
     *
     * @return test role value
     */
    public static Role getDefaultAdminRole() {
        return new Role(ID, ADMIN);
    }

    /**
     * Returns the default admin role list fixture used in tests.
     *
     * @return test role values
     */
    public static List<Role> getDefaultAdminRoleList() {
        return Collections.singletonList(getDefaultAdminRole());
    }

    /**
     * Returns the default role list fixture used in tests.
     *
     * @return test role values
     */
    public static List<Role> getDefaultRoleList() {
        return Collections.singletonList(getDefaultRole());
    }

    /**
     * Returns the default role dto fixture used in tests.
     *
     * @return test role dto value
     */
    public static RoleDto getDefaultRoleDto() {
        return new RoleDto(ID, NAME);
    }

    /**
     * Returns the default role dto list fixture used in tests.
     *
     * @return test role dto values
     */
    public static List<RoleDto> getDefaultRoleDtoList() {
        return Collections.singletonList(getDefaultRoleDto());
    }

    /**
     * Returns the default role names list fixture used in tests.
     *
     * @return test string values
     */
    public static List<String> getDefaultRoleNamesList() {
        return Collections.singletonList(NAME);
    }
}

