package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class ReportConstants {

    public static final int ID = 1;

    public static final BigDecimal INCOMES = new BigDecimal("10000.00");

    public static final LocalDate START_DATE = LocalDate.of(1900, 1, 1);

    public static final LocalDate END_DATE = LocalDate.of(2100, 1, 1);

    private ReportConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
