package bg.vdrenkov.cineledger.utils.constants;

public final class JwtConstants {

  public static final long JWT_TOKEN_VALIDITY_SECONDS = 60L * 60L;
  public static final long JWT_TOKEN_VALIDITY_MILLIS = JWT_TOKEN_VALIDITY_SECONDS * 1000L;
  public static final String JWT_COOKIE_NAME = "CINELEDGER_AUTH";

  private JwtConstants() throws IllegalAccessException {
    throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}

