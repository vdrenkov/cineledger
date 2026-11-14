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

public final class ReviewConstants {

    public static final int ID = 1;

    public static final double RATING = 1.1;

    public static final String REVIEW_TEXT = "review text";

    public static final LocalDate DATE_MODIFIED = LocalDate.of(2024, 1, 1);

    public static final Movie REVIEW_MOVIE = MovieFactory.getDefaultMovie();

    public static final MovieDto REVIEW_MOVIE_DTO = MovieFactory.getDefaultMovieDto();

    public static final Cinema REVIEW_CINEMA = CinemaFactory.getDefaultCinema();

    public static final CinemaDto REVIEW_CINEMA_DTO = CinemaFactory.getDefaultCinemaDto();

    public static final User REVIEW_USER = UserFactory.getDefaultUser();

    public static final UserDto REVIEW_USER_DTO = UserFactory.getDefaultUserDto();

    private ReviewConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
