package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class CinemaConstants {

    public static final int ID = 1;

    public static final String ADDRESS = "Address";

    public static final String CITY = "City";

    public static final int AVERAGE_RATING = 5;

    private CinemaConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
