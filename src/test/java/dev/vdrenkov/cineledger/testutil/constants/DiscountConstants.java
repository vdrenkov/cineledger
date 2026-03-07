package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

/**
 * Collects reusable discount constants for tests.
 */
public final class DiscountConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default type used in tests.
     */
    public static final String TYPE = "Military";
    /**
     * Provides the default code used in tests.
     */
    public static final String CODE = "0000";
    /**
     * Provides the default percentage used in tests.
     */
    public static final int PERCENTAGE = 10;

    private DiscountConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


