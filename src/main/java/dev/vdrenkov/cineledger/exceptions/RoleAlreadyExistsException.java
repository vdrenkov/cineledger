package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested role already exists.
 */
public class RoleAlreadyExistsException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}


