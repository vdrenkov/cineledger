package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class ReviewNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = -5904097386891683607L;

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
