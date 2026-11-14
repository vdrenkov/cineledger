package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class RoleNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = 4288804808788995850L;

    public RoleNotFoundException(String message) {
        super(message);
    }
}
