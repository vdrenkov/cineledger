package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class NoAvailableTicketsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1491924117941771259L;

    public NoAvailableTicketsException(String message) {
        super(message);
    }
}
