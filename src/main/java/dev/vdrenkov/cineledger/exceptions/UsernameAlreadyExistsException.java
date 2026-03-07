package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested username already exists.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}


