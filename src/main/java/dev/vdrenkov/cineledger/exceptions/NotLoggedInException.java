package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the current request requires an authenticated user.
 */
public class NotLoggedInException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 9072182825040577244L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public NotLoggedInException(String message) {
        super(message);
    }
}


