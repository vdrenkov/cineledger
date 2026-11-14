package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class HallNotAvailableException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5230654874246110433L;

    public HallNotAvailableException(String message) {
        super(message);
    }

}
