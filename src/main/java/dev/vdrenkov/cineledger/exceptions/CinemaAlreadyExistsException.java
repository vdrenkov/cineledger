package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class CinemaAlreadyExistsException extends RuntimeException {
    @Serial private static final long serialVersionUID = 5790359979792968056L;

    public CinemaAlreadyExistsException(String message) {
        super(message);
    }
}
