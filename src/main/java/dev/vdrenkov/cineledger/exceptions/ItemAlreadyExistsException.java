package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class ItemAlreadyExistsException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = -7133192092794659772L;

    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}
