package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class CinemaNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = -1141551388494735118L;

    public CinemaNotFoundException(String message) {
        super(message);
    }
}
