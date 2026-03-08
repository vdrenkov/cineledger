package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps movie domain models to DTO representations used by the API.
 */
public final class MovieMapper {
    private static final Logger log = LoggerFactory.getLogger(MovieMapper.class);

    private MovieMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps movie values to movie dto values.
     *
     * @param movie
     *     movie entity to transform
     * @return movie dto result
     */
    public static MovieDto mapMovieToMovieDto(Movie movie) {
        log.info("Mapping the movie {} to a movie DTO", movie.getTitle());
        return new MovieDto(movie.getId(), movie.getTitle(), movie.getAverageRating(), movie.getDescription(),
            movie.getReleaseDate(), movie.getRuntime(), CategoryMapper.mapCategoryToCategoryDto(movie.getCategory()));
    }

    /**
     * Maps movie list values to movie dto list values.
     *
     * @param movies
     *     movie entities to transform
     * @return matching movie dto values
     */
    public static List<MovieDto> mapMovieListToMovieDtoList(List<Movie> movies) {
        return movies.stream().map(MovieMapper::mapMovieToMovieDto).toList();
    }
}

