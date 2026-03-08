package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

/**
 * Collects reusable http cookie constants for tests.
 */
public final class HttpCookieConstants {

    /**
     * Provides the default cookie name used in tests.
     */
    public static final String COOKIE_NAME = "Cookie";
    /**
     * Provides the default cookie value used in tests.
     */
    public static final String COOKIE_VALUE = "Value";

    private HttpCookieConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


