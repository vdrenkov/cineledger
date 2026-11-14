package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class DiscountAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2768977154786826477L;

    public DiscountAlreadyExistsException(String message) {
        super(message);
    }
}
