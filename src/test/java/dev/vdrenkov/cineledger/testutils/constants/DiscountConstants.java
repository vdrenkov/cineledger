package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class DiscountConstants {

    public static final int ID = 1;

    public static final String TYPE = "Military";

    public static final String CODE = "0000";

    public static final int PERCENTAGE = 10;

    private DiscountConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
