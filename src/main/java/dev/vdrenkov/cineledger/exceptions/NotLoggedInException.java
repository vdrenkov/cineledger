package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class NotLoggedInException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 9072182825040577244L;

    public NotLoggedInException(String message) {
        super(message);
    }
}
