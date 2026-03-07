package bg.vdrenkov.cineledger.testUtils.constants;

import bg.vdrenkov.cineledger.models.dtos.HallDto;
import bg.vdrenkov.cineledger.models.dtos.MovieDto;
import bg.vdrenkov.cineledger.models.dtos.ProgramDto;
import bg.vdrenkov.cineledger.testUtils.factories.HallFactory;
import bg.vdrenkov.cineledger.testUtils.factories.MovieFactory;
import bg.vdrenkov.cineledger.testUtils.factories.ProgramFactory;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalTime;

public final class ProjectionConstants {

    public static final int ID = 1;
    public static final double PRICE = 10;
    public static final LocalTime START_TIME = LocalTime.of(1, 0, 1);
    public static final HallDto PROJECTION_HALL_DTO = HallFactory.getDefaultHallDto();
    public static final MovieDto PROJECTION_MOVIE_DTO = MovieFactory.getDefaultMovieDto();
    public static final ProgramDto PROJECTION_PROGRAM_DTO = ProgramFactory.getDefaultProgramDto();

    private ProjectionConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


