package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the requested order could not be found.
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}


