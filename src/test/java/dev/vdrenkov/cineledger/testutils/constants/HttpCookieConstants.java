package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class HttpCookieConstants {

    public static final String COOKIE_NAME = "Cookie";

    public static final String COOKIE_VALUE = "Value";

    private HttpCookieConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
