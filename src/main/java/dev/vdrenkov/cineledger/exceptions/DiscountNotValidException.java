package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class DiscountNotValidException extends RuntimeException {
    @Serial private static final long serialVersionUID = 5903357858011911292L;

    public DiscountNotValidException(String message) {
        super(message);
    }
}
