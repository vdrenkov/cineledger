package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class CategoryNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = -4243365692979270023L;

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
