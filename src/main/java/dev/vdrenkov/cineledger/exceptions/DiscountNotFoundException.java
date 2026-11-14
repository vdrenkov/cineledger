package dev.vdrenkov.cineledger.exceptions;

import java.io.Serial;

public class DiscountNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = -1879239370296798893L;

    public DiscountNotFoundException(String message) {
        super(message);
    }
}
