package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.entities.Movie;
import bg.vdrenkov.cineledger.models.requests.MovieRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import bg.vdrenkov.cineledger.models.dtos.MovieDto;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.DESCRIPTION;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.RATING;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.RELEASE_DATE;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.RUNTIME;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.TITLE;

public final class MovieFactory {

  private MovieFactory() throws IllegalAccessException {
    throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static MovieRequest getDefaultMovieRequest() {
    return new MovieRequest(TITLE, DESCRIPTION, RELEASE_DATE, RUNTIME, ID);
  }

  public static Movie getDefaultMovie() {
    return new Movie(ID, TITLE, RATING, DESCRIPTION, RELEASE_DATE, RUNTIME,
                     CategoryFactory.getDefaultCategory());
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


