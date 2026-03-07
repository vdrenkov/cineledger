package bg.vdrenkov.cineledger.testUtils.constants;

import bg.vdrenkov.cineledger.models.dtos.CinemaDto;
import bg.vdrenkov.cineledger.models.dtos.MovieDto;
import bg.vdrenkov.cineledger.models.dtos.UserDto;
import bg.vdrenkov.cineledger.models.entities.Cinema;
import bg.vdrenkov.cineledger.models.entities.Movie;
import bg.vdrenkov.cineledger.models.entities.User;
import bg.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
import bg.vdrenkov.cineledger.testUtils.factories.MovieFactory;
import bg.vdrenkov.cineledger.testUtils.factories.UserFactory;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

public final class ReviewConstants {

    public static final int ID = 1;
    public static final double RATING = 1.1;
    public static final String REVIEW_TEXT = "review text";
    public static final LocalDate DATE_MODIFIED = LocalDate.of(2024, 1, 1);
    public static final LocalDate NOW = LocalDate.now();
    public static final Movie REVIEW_MOVIE = MovieFactory.getDefaultMovie();
    public static final MovieDto REVIEW_MOVIE_DTO = MovieFactory.getDefaultMovieDto();
    public static final Cinema REVIEW_CINEMA = CinemaFactory.getDefaultCinema();
    public static final CinemaDto REVIEW_CINEMA_DTO = CinemaFactory.getDefaultCinemaDto();
    public static final User REVIEW_USER = UserFactory.getDefaultUser();
    public static final UserDto REVIEW_USER_DTO = UserFactory.getDefaultUserDto();

    private ReviewConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


