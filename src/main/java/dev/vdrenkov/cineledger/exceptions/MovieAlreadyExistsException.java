package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class MovieAlreadyExistsException extends IllegalArgumentException {
    @Serial private static final long serialVersionUID = -7185141585628484776L;

    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}
