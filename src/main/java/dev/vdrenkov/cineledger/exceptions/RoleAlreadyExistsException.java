package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class RoleAlreadyExistsException extends RuntimeException {
    @Serial private static final long serialVersionUID = 2138796508564350290L;

    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}
