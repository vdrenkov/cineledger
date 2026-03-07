package dev.vdrenkov.cineledger.testUtils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class HallConstants {

    public static final int CAPACITY = 100;
    public static final int ID = 1;

    private HallConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


