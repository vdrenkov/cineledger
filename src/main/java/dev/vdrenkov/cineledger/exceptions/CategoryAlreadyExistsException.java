package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class CategoryAlreadyExistsException extends RuntimeException {
    @Serial private static final long serialVersionUID = -3516632937559308083L;

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
