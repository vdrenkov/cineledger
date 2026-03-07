package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

/**
 * Collects reusable cinema constants for tests.
 */
public final class CinemaConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default address used in tests.
     */
    public static final String ADDRESS = "Address";
    /**
     * Provides the default city used in tests.
     */
    public static final String CITY = "City";
    /**
     * Provides the default average rating used in tests.
     */
    public static final int AVERAGE_RATING = 5;

    private CinemaConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


