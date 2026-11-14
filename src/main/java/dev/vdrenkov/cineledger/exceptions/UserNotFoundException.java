package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = -8961627294884145213L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
