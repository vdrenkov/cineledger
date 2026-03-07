package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that a submitted date violates the application validation rules.
 */
public class DateNotValidException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public DateNotValidException(String message) {
        super(message);
    }
}


