package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested program already exists.
 */
public class ProgramAlreadyExistsException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ProgramAlreadyExistsException(String message) {
        super(message);
    }
}


