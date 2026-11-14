package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class ProjectionNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2573394428928006927L;

    public ProjectionNotFoundException(String message) {
        super(message);
    }
}
