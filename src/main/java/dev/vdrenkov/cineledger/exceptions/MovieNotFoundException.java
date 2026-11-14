package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class MovieNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6570840463533376412L;

    public MovieNotFoundException(String message) {
        super(message);
    }
}
