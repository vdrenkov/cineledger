package bg.vdrenkov.cineledger.testUtils.constants;

import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class RoleConstants {

  public static final int ID = 1;
  public static final String NAME = "USER";
  public static final String ADMIN = "ADMIN";

  private RoleConstants() throws IllegalAccessException {
    throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}


