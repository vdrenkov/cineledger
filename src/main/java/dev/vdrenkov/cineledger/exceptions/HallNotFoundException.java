package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class HallNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = -478944443126647862L;

    public HallNotFoundException(String message) {
        super(message);
    }
}
