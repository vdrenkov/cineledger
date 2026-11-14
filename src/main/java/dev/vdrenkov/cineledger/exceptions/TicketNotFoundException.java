package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class TicketNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = -8020090034668360149L;

    public TicketNotFoundException(String message) {
        super(message);
    }
}
