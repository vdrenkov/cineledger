package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class NotAuthorizedException extends RuntimeException {
    @Serial private static final long serialVersionUID = -4660620627455830124L;

    public NotAuthorizedException(String message) {
        super(message);
    }
}
