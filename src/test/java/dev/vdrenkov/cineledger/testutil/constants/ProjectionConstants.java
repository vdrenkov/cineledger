package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.testutil.factories.HallFactory;
import dev.vdrenkov.cineledger.testutil.factories.MovieFactory;
import dev.vdrenkov.cineledger.testutil.factories.ProgramFactory;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalTime;

/**
 * Collects reusable projection constants for tests.
 */
public final class ProjectionConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default price used in tests.
     */
    public static final double PRICE = 10;
    /**
     * Provides the default start time used in report-related tests.
     */
    public static final LocalTime START_TIME = LocalTime.of(1, 0, 1);
    /**
     * Provides the default projection hall dto used in tests.
     */
    public static final HallDto PROJECTION_HALL_DTO = HallFactory.getDefaultHallDto();
    /**
     * Provides the default projection movie dto used in tests.
     */
    public static final MovieDto PROJECTION_MOVIE_DTO = MovieFactory.getDefaultMovieDto();
    /**
     * Provides the default projection program dto used in tests.
     */
    public static final ProgramDto PROJECTION_PROGRAM_DTO = ProgramFactory.getDefaultProgramDto();

    private ProjectionConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}



