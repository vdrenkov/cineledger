package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

/**
 * Collects reusable report constants for tests.
 */
public final class ReportConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default incomes used in tests.
     */
    public static final double INCOMES = 10000;
    /**
     * Provides the default start date used in report-related tests.
     */
    public static final LocalDate START_DATE = LocalDate.of(1900, 1, 1);
    /**
     * Provides the default end date used in report-related tests.
     */
    public static final LocalDate END_DATE = LocalDate.of(2100, 1, 1);

    private ReportConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}



