package bg.vdrenkov.cineledger.testUtils.constants;

import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

public final class CinemaConstants {

    public static final int ID = 1;
    public static final String ADDRESS = "Address";
    public static final String CITY = "City";
    public static final int AVERAGE_RATING = 5;

    private CinemaConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


