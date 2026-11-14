package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.testutils.factories.CinemaFactory;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

public final class ProgramConstants {

    public static final int ID = 1;

    public static final LocalDate PAST_DATE = LocalDate.of(2000, 1, 1);

    public static final LocalDate DATE = LocalDate.of(2100, 1, 1);

    public static final Cinema PROGRAM_CINEMA = CinemaFactory.getDefaultCinema();

    private ProgramConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
