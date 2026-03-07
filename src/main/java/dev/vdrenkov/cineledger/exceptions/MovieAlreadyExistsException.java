package dev.vdrenkov.cineledger.exceptions;

public class MovieAlreadyExistsException extends IllegalArgumentException {

    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}


