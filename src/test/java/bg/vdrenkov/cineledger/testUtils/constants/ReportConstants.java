package bg.vdrenkov.cineledger.testUtils.constants;

import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

public final class ReportConstants {

  public static final int ID = 1;
  public static final double INCOMES = 10000;
  public static final LocalDate START_DATE = LocalDate.of(1900, 1, 1);
  public static final LocalDate END_DATE = LocalDate.of(2100, 1, 1);

  private ReportConstants() throws IllegalAccessException {
    throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}


