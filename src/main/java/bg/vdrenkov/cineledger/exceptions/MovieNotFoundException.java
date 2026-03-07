package bg.vdrenkov.cineledger.exceptions;

public class MovieNotFoundException extends RuntimeException {

  public MovieNotFoundException(String message) {
    super(message);
  }
}


