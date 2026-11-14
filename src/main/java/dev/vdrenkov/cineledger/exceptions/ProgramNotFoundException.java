package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class ProgramNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = -5111116158791848252L;

    public ProgramNotFoundException(String message) {
        super(message);
    }
}
