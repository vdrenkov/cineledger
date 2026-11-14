package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class DateNotValidException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5383611895522283042L;

    public DateNotValidException(String message) {
        super(message);
    }
}
