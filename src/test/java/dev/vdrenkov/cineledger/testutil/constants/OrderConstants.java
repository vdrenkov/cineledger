package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

/**
 * Collects reusable order constants for tests.
 */
public final class OrderConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default year used in tests.
     */
    public static final int YEAR = 2000;
    /**
     * Provides the default month used in tests.
     */
    public static final int MONTH = 5;
    /**
     * Provides the default day used in tests.
     */
    public static final int DAY = 10;
    /**
     * Provides the default date of purchase used in tests.
     */
    public static final LocalDate DATE_OF_PURCHASE = LocalDate.of(YEAR, MONTH, DAY);
    /**
     * Provides the default total price used in tests.
     */
    public static final double TOTAL_PRICE = 100;

    private OrderConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


