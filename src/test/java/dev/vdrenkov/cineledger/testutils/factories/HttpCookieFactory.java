package dev.vdrenkov.cineledger.testutils.factories;

import org.springframework.http.HttpCookie;

import static dev.vdrenkov.cineledger.testutils.constants.HttpCookieConstants.COOKIE_NAME;
import static dev.vdrenkov.cineledger.testutils.constants.HttpCookieConstants.COOKIE_VALUE;

/**
 * Provides reusable http cookie fixtures for tests.
 */
public final class HttpCookieFactory {

    private HttpCookieFactory() {
        throw new IllegalStateException("Utility class. Do not instantiate!");
    }

    /**
     * Returns the default http cookie fixture used in tests.
     *
     * @return test http cookie value
     */
    public static HttpCookie getDefaultHttpCookie() {
        return new HttpCookie(COOKIE_NAME, COOKIE_VALUE);
    }
}


