package dev.vdrenkov.cineledger.utils.constants;

/**
 * Defines JWT token and cookie settings shared by the authentication flow.
 */
public final class JwtConstants {

    /** Default token lifetime expressed in seconds. */
    public static final long JWT_TOKEN_VALIDITY_SECONDS = 60L * 60L;
    /** Default token lifetime expressed in milliseconds. */
    public static final long JWT_TOKEN_VALIDITY_MILLIS = JWT_TOKEN_VALIDITY_SECONDS * 1000L;
    /** Name of the authentication cookie carrying the JWT token. */
    public static final String JWT_COOKIE_NAME = "CINELEDGER_AUTH";

    private JwtConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


