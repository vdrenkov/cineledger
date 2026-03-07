package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.dtos.RoleDto;
import bg.vdrenkov.cineledger.models.entities.Role;
import bg.vdrenkov.cineledger.models.requests.RoleRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.RoleConstants.ADMIN;
import static bg.vdrenkov.cineledger.testUtils.constants.RoleConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.RoleConstants.NAME;

public final class RoleFactory {

    private RoleFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static RoleRequest getDefaultRoleRequest() {
        return new RoleRequest(NAME);
    }

    public static Role getDefaultRole() {
        return new Role(ID, NAME);
    }

    public static Role getDefaultAdminRole() {
        return new Role(ID, ADMIN);
    }

    public static List<Role> getDefaultAdminRoleList() {
        return Collections.singletonList(getDefaultAdminRole());
    }

    public static List<Role> getDefaultRoleList() {
        return Collections.singletonList(getDefaultRole());
    }

    public static RoleDto getDefaultRoleDto() {
        return new RoleDto(ID, NAME);
    }

    public static List<RoleDto> getDefaultRoleDtoList() {
        return Collections.singletonList(getDefaultRoleDto());
    }

    public static List<String> getDefaultRoleNamesList() {
        return Collections.singletonList(NAME);
    }
}

