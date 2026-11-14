package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class ProgramAlreadyExistsException extends RuntimeException {
    @Serial private static final long serialVersionUID = -8325731241704512988L;

    public ProgramAlreadyExistsException(String message) {
        super(message);
    }
}
