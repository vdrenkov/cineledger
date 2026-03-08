package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the provided discount code does not satisfy the expected format.
 */
public class DiscountNotValidException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5903357858011911292L;

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


