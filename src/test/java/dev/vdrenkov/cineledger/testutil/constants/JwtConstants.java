package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

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

    private JwtConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


