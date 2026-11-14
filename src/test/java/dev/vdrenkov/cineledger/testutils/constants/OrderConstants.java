package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class OrderConstants {

    public static final int ID = 1;

    public static final int YEAR = 2000;

    public static final int MONTH = 5;

    public static final int DAY = 10;

    public static final LocalDate DATE_OF_PURCHASE = LocalDate.of(YEAR, MONTH, DAY);

    public static final BigDecimal TOTAL_PRICE = new BigDecimal("100.00");

    private OrderConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
