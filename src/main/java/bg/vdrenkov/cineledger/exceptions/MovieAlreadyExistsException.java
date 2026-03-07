package bg.vdrenkov.cineledger.exceptions;

public class MovieAlreadyExistsException extends IllegalArgumentException {

    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}


