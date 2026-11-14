package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class RoleNotChosenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3264444334288365668L;

    public RoleNotChosenException(String message) {
        super(message);
    }
}
