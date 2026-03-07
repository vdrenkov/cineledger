package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Date;

/**
 * Collects reusable jwt constants for tests.
 */
public final class JwtConstants {

    /**
     * Provides the default jwt token used in tests.
     */
    public static final String JWT_TOKEN = "JwtToken";
    /**
     * Provides the default user role used in tests.
     */
    public static final String USER_ROLE = "ADMIN";
    /**
     * Provides the default jwt password used in tests.
     */
    public static final String JWT_PASSWORD = "Password";
    /**
     * Provides the default jwt username used in tests.
     */
    public static final String JWT_USERNAME = "Username";
    /**
     * Provides the default jwt cookie name used in tests.
     */
    public static final String JWT_COOKIE_NAME = "CINELEDGER_AUTH";
    /**
     * Provides the current date value used in time-sensitive tests.
     */
    public static final Date NOW = new Date();
    private static final long validityInMilliseconds = 3600000;
    /**
     * Provides the default expiration used in tests.
     */
    public static final Date EXPIRATION = new Date(NOW.getTime() + validityInMilliseconds);

    private JwtConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}

