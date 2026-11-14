package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class UserEmailAlreadyExistsException extends RuntimeException {
    @Serial private static final long serialVersionUID = -1136346203858810779L;

    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }
}
