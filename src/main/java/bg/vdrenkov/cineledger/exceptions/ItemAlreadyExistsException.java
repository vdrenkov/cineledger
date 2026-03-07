package bg.vdrenkov.cineledger.exceptions;

public class ItemAlreadyExistsException extends IllegalArgumentException {

    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}


