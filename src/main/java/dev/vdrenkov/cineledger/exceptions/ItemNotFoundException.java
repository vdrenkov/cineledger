package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class ItemNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5738561736569945881L;

    public ItemNotFoundException(String message) {
        super(message);
    }
}
