package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested discount already exists.
 */
public class DiscountAlreadyExistsException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public DiscountAlreadyExistsException(String message) {
        super(message);
    }
}


