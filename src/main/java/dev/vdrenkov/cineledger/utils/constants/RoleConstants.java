package dev.vdrenkov.cineledger.utils.constants;

/**
 * Defines the canonical role names used by authentication and bootstrap logic.
 */
public final class RoleConstants {

    /** Default administrator role name. */
    public static final String DEFAULT_ADMIN_ROLE = "ADMIN";
    /** Default cinema vendor role name. */
    public static final String DEFAULT_VENDOR_ROLE = "VENDOR";
    /** Default end-user role name. */
    public static final String DEFAULT_USER_ROLE = "USER";

    private RoleConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}



