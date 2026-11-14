package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.math.BigDecimal;

public final class ItemConstants {

    public static final int ID = 1;

    public static final String NAME = "Name";

    public static final BigDecimal PRICE = new BigDecimal("13.00");

    public static final int QUANTITY = 55;

    public static final boolean IS_BELLOW = false;

    private ItemConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
