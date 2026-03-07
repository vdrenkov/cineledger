package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps movie domain models to DTO representations used by the API.
 */
@Component
public class MovieMapper {
    private static final Logger log = LoggerFactory.getLogger(MovieMapper.class);

    private final CategoryMapper categoryMapper;

    /**
     * Creates a new movie mapper with its required collaborators.
     *
     * @param categoryMapper
     *     category mapper used by the operation
     */
    @Autowired
    public MovieMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * Maps movie values to movie dto values.
     *
     * @param movie
     *     movie entity to transform
     * @return movie dto result
     */
    public MovieDto mapMovieToMovieDto(Movie movie) {
        log.info(String.format("The movie %s is being mapped to a movie DTO", movie.getTitle()));
        return new MovieDto(movie.getId(), movie.getTitle(), movie.getAverageRating(), movie.getDescription(),
            movie.getReleaseDate(), movie.getRuntime(), categoryMapper.mapCategoryToCategoryDto(movie.getCategory()));
    }

    /**
     * Maps movie list values to movie dto list values.
     *
     * @param movies
     *     movie entities to transform
     * @return matching movie dto values
     */
    public List<MovieDto> mapMovieListToMovieDtoList(List<Movie> movies) {
        return movies.stream().map(this::mapMovieToMovieDto).collect(Collectors.toList());
    }
}

