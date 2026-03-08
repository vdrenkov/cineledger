package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

/**
 * Collects reusable user constants for tests.
 */
public final class UserConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default username used in tests.
     */
    public static final String USERNAME = "Username";
    /**
     * Provides the default password used in tests.
     */
    public static final String PASSWORD = "password";
    /**
     * Provides the default email used in tests.
     */
    public static final String EMAIL = "test@email.com";
    /**
     * Provides the default first name used in tests.
     */
    public static final String FIRST_NAME = "First name";
    /**
     * Provides the default last name used in tests.
     */
    public static final String LAST_NAME = "Last name";
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
    public static final int DAY = 3;
    /**
     * Provides the default join date used in tests.
     */
    public static final LocalDate JOIN_DATE = LocalDate.of(YEAR, MONTH, DAY);

    private UserConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}



