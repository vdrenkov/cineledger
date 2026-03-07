package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

/**
 * Collects reusable role constants for tests.
 */
public final class RoleConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default name used in tests.
     */
    public static final String NAME = "USER";
    /**
     * Provides the default admin used in tests.
     */
    public static final String ADMIN = "ADMIN";

    private RoleConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


