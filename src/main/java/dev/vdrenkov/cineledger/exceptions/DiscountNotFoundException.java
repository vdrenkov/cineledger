package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested discount could not be found.
 */
public class DiscountNotFoundException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public DiscountNotFoundException(String message) {
        super(message);
    }
}


