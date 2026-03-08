package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

/**
 * Signals that the requested role already exists.
 */
public class RoleAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2138796508564350290L;

    /**
     * Creates the exception with the supplied message.
     *
     * @param message
     *     exception message to propagate
     */
    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}


