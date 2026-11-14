package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.requests.MovieRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.DESCRIPTION;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.RATING;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.RELEASE_DATE;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.RUNTIME;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.TITLE;

public final class MovieFactory {

    private MovieFactory() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static MovieRequest getDefaultMovieRequest() {
        return new MovieRequest(TITLE, DESCRIPTION, RELEASE_DATE, RUNTIME, ID);
    }

    public static Movie getDefaultMovie() {
        return new Movie(ID, TITLE, RATING, DESCRIPTION, RELEASE_DATE, RUNTIME, CategoryFactory.getDefaultCategory());
    }

    public static List<Movie> getDefaultMovieList() {
        return Collections.singletonList(getDefaultMovie());
    }

    public static MovieDto getDefaultMovieDto() {
        return new MovieDto(ID, TITLE, RATING, DESCRIPTION, RELEASE_DATE, RUNTIME,
            CategoryFactory.getDefaultCategoryDto());
    }

    public static List<MovieDto> getDefaultMovieDtoList() {
        return Collections.singletonList(getDefaultMovieDto());
    }
}
