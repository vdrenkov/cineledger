package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class NoAvailableItemsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7448656300640151601L;

    public NoAvailableItemsException(String message) {
        super(message);
    }
}
