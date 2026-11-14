package dev.vdrenkov.cineledger.utils.constants;

public final class UserConstants {

    public static final String DEFAULT_GUEST_USERNAME = "anonymousUser";

    private UserConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
