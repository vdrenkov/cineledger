package dev.vdrenkov.cineledger.utils.constants;

/**
 * Defines user-related default values shared across authentication flows.
 */
public final class UserConstants {

    /** Username assigned by Spring Security to anonymous requests. */
    public static final String DEFAULT_GUEST_USERNAME = "anonymousUser";

    private UserConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


