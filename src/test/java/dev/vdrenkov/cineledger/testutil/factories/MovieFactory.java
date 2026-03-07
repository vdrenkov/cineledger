package dev.vdrenkov.cineledger.testutil.factories;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.requests.MovieRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutil.constants.MovieConstants.DESCRIPTION;
import static dev.vdrenkov.cineledger.testutil.constants.MovieConstants.ID;
import static dev.vdrenkov.cineledger.testutil.constants.MovieConstants.RATING;
import static dev.vdrenkov.cineledger.testutil.constants.MovieConstants.RELEASE_DATE;
import static dev.vdrenkov.cineledger.testutil.constants.MovieConstants.RUNTIME;
import static dev.vdrenkov.cineledger.testutil.constants.MovieConstants.TITLE;

/**
 * Provides reusable movie fixtures for tests.
 */
public final class MovieFactory {

    private MovieFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default movie request fixture used in tests.
     *
     * @return test movie request value
     */
    public static MovieRequest getDefaultMovieRequest() {
        return new MovieRequest(TITLE, DESCRIPTION, RELEASE_DATE, RUNTIME, ID);
    }

    /**
     * Returns the default movie fixture used in tests.
     *
     * @return test movie value
     */
    public static Movie getDefaultMovie() {
        return new Movie(ID, TITLE, RATING, DESCRIPTION, RELEASE_DATE, RUNTIME, CategoryFactory.getDefaultCategory());
    }

    /**
     * Returns the default movie list fixture used in tests.
     *
     * @return test movie values
     */
    public static List<Movie> getDefaultMovieList() {
        return Collections.singletonList(getDefaultMovie());
    }

    /**
     * Returns the default movie dto fixture used in tests.
     *
     * @return test movie dto value
     */
    public static MovieDto getDefaultMovieDto() {
        return new MovieDto(ID, TITLE, RATING, DESCRIPTION, RELEASE_DATE, RUNTIME,
            CategoryFactory.getDefaultCategoryDto());
    }

    /**
     * Returns the default movie dto list fixture used in tests.
     *
     * @return test movie dto values
     */
    public static List<MovieDto> getDefaultMovieDtoList() {
        return Collections.singletonList(getDefaultMovieDto());
    }
}


