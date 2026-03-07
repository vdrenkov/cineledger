package bg.vdrenkov.cineledger.utils.constants;

public final class UserConstants {

  public static final String DEFAULT_GUEST_USERNAME = "anonymousUser";

  private UserConstants() throws IllegalAccessException {
    throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}


