package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class RoleConstants {

    public static final int ID = 1;

    public static final String NAME = "USER";

    public static final String ADMIN = "ADMIN";

    private RoleConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
