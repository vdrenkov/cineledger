package dev.vdrenkov.cineledger.utils.constants;

public final class RoleConstants {

    public static final String DEFAULT_ADMIN_ROLE = "ADMIN";

    public static final String DEFAULT_VENDOR_ROLE = "VENDOR";

    public static final String DEFAULT_USER_ROLE = "USER";

    private RoleConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
