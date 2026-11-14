package dev.vdrenkov.cineledger.testutils.factories;

import org.springframework.http.HttpCookie;

import static dev.vdrenkov.cineledger.testutils.constants.HttpCookieConstants.COOKIE_NAME;
import static dev.vdrenkov.cineledger.testutils.constants.HttpCookieConstants.COOKIE_VALUE;

public final class HttpCookieFactory {

    private HttpCookieFactory() {
        throw new IllegalStateException("Utility class. Do not instantiate!");
    }

    public static HttpCookie getDefaultHttpCookie() {
        return new HttpCookie(COOKIE_NAME, COOKIE_VALUE);
    }
}
