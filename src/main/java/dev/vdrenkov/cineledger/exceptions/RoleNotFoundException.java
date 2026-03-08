package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested role could not be found.
 */
public class RoleNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4288804808788995850L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public RoleNotFoundException(String message) {
        super(message);
    }
}


