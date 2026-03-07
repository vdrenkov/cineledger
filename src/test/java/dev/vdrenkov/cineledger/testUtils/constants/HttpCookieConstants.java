package dev.vdrenkov.cineledger.testUtils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class HttpCookieConstants {

    public static final String COOKIE_NAME = "Cookie";
    public static final String COOKIE_VALUE = "Value";

    private HttpCookieConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}

