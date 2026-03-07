package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that a role-dependent operation was requested without selecting roles.
 */
public class RoleNotChosenException extends RuntimeException {
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


