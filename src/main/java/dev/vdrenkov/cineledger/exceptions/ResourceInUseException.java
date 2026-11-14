package dev.vdrenkov.cineledger.exceptions;

public class ResourceInUseException extends RuntimeException {
    public ResourceInUseException(final String message) {
        super(message);
    }
}
