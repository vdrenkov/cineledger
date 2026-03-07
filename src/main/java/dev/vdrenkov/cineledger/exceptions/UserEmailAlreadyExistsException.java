package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested user email already exists.
 */
public class UserEmailAlreadyExistsException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }
}


