package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested discount could not be found.
 */
public class DiscountNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1879239370296798893L;

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


