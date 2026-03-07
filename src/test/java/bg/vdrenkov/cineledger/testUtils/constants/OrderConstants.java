package bg.vdrenkov.cineledger.testUtils.constants;

import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

public final class OrderConstants {

  public static final int ID = 1;
  public static final int YEAR = 2000;
  public static final int MONTH = 5;
  public static final int DAY = 10;
  public static final LocalDate DATE_OF_PURCHASE = LocalDate.of(YEAR, MONTH, DAY);
  public static final double TOTAL_PRICE = 100;

  private OrderConstants() throws IllegalAccessException {
    throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}


