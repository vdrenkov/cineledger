package dev.vdrenkov.cineledger.exceptions;

/**
 * Signals that the provided discount code does not satisfy the expected format.
 */
public class DiscountNotValidException extends RuntimeException {
    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public DiscountNotValidException(String message) {
        super(message);
    }
}


