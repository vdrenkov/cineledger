package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that a role-dependent operation was requested without selecting roles.
 */
public class RoleNotChosenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3264444334288365668L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public RoleNotChosenException(String message) {
        super(message);
    }
}


