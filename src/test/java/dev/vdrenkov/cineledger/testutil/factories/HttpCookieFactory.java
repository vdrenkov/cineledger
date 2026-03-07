package dev.vdrenkov.cineledger.testutil.factories;

import org.springframework.http.HttpCookie;

import static dev.vdrenkov.cineledger.testutil.constants.HttpCookieConstants.COOKIE_NAME;
import static dev.vdrenkov.cineledger.testutil.constants.HttpCookieConstants.COOKIE_VALUE;

/**
 * Provides reusable http cookie fixtures for tests.
 */
public final class HttpCookieFactory {

    private HttpCookieFactory() throws IllegalAccessException {
        throw new IllegalAccessException();
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


