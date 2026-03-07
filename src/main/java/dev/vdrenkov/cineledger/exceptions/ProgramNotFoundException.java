package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested program could not be found.
 */
public class ProgramNotFoundException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public ProgramNotFoundException(String message) {
        super(message);
    }
}


