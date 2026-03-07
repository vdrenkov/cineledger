package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.testutil.factories.CinemaFactory;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

/**
 * Collects reusable program constants for tests.
 */
public final class ProgramConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public final static int ID = 1;
    /**
     * Provides the past date value used in tests.
     */
    public final static LocalDate PAST_DATE = LocalDate.of(2000, 1, 1);
    /**
     * Provides the default date used in tests.
     */
    public final static LocalDate DATE = LocalDate.now().plusDays(30);
    /**
     * Provides the default program cinema used in tests.
     */
    public static final Cinema PROGRAM_CINEMA = CinemaFactory.getDefaultCinema();

    private ProgramConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


