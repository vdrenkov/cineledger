package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.testutils.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutils.factories.MovieFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

/**
 * Collects reusable review constants for tests.
 */
public final class ReviewConstants {

    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default rating used in tests.
     */
    public static final double RATING = 1.1;
    /**
     * Provides the default review text used in tests.
     */
    public static final String REVIEW_TEXT = "review text";
    /**
     * Provides the default date modified used in tests.
     */
    public static final LocalDate DATE_MODIFIED = LocalDate.of(2024, 1, 1);
    /**
     * Provides the default review movie used in tests.
     */
    public static final Movie REVIEW_MOVIE = MovieFactory.getDefaultMovie();
    /**
     * Provides the default review movie dto used in tests.
     */
    public static final MovieDto REVIEW_MOVIE_DTO = MovieFactory.getDefaultMovieDto();
    /**
     * Provides the default review cinema used in tests.
     */
    public static final Cinema REVIEW_CINEMA = CinemaFactory.getDefaultCinema();
    /**
     * Provides the default review cinema dto used in tests.
     */
    public static final CinemaDto REVIEW_CINEMA_DTO = CinemaFactory.getDefaultCinemaDto();
    /**
     * Provides the default review user used in tests.
     */
    public static final User REVIEW_USER = UserFactory.getDefaultUser();
    /**
     * Provides the default review user dto used in tests.
     */
    public static final UserDto REVIEW_USER_DTO = UserFactory.getDefaultUserDto();

    private ReviewConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}



