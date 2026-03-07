package dev.vdrenkov.cineledger.testUtils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class DiscountConstants {

    public static final int ID = 1;
    public static final String TYPE = "Military";
    public static final String CODE = "0000";
    public static final int PERCENTAGE = 10;

    private DiscountConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


