package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

/**
 * Collects reusable ticket constants for tests.
 */
public final class TicketConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default date of purchase used in tests.
     */
    public static final LocalDate DATE_OF_PURCHASE = LocalDate.of(1999, 1, 1);

    private TicketConstants() throws IllegalAccessError {
        throw new IllegalAccessError(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}



