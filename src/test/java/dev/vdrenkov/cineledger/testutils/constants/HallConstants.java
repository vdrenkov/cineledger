package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

/**
 * Collects reusable hall constants for tests.
 */
public final class HallConstants {

    /**
     * Provides the default capacity used in tests.
     */
    public static final int CAPACITY = 100;
    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;

    private HallConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


