package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class UsernameAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6239758079714036337L;

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
