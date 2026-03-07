package dev.vdrenkov.cineledger.testUtils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class CategoryConstants {

    public static final int ID = 1;
    public static final String NAME = "Name";

    private CategoryConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}

