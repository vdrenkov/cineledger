package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class OrderNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7185545981023502498L;

    public OrderNotFoundException(String message) {
        super(message);
    }
}
