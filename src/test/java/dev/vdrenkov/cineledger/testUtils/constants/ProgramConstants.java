package dev.vdrenkov.cineledger.testUtils.constants;

import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

public final class ProgramConstants {

    public final static int ID = 1;
    public final static LocalDate PAST_DATE = LocalDate.of(2000, 1, 1);
    public final static LocalDate DATE = LocalDate.now().plusDays(30);
    public static final Cinema PROGRAM_CINEMA = CinemaFactory.getDefaultCinema();

    private ProgramConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


