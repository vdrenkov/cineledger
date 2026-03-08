package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

/**
 * Collects reusable item constants for tests.
 */
public final class ItemConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default name used in tests.
     */
    public static final String NAME = "Name";
    /**
     * Provides the default price used in tests.
     */
    public static final double PRICE = 13;
    /**
     * Provides the default quantity used in tests.
     */
    public static final int QUANTITY = 55;
    /**
     * Provides the default is bellow used in tests.
     */
    public static final boolean IS_BELLOW = false;

    private ItemConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}



