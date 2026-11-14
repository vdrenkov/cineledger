package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class JwtConstants {

    public static final String JWT_TOKEN = "JwtToken";

    public static final String USER_ROLE = "ADMIN";

    public static final String JWT_PASSWORD = "Password";

    public static final String JWT_USERNAME = "Username";

    private JwtConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
