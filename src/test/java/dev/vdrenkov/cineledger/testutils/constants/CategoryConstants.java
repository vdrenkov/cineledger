package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class CategoryConstants {

    public static final int ID = 1;

    public static final String NAME = "Name";

    private CategoryConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
