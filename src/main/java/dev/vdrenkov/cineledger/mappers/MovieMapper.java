package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;

import java.util.List;

public final class MovieMapper {

    private MovieMapper() {}

    public static MovieDto mapMovieToMovieDto(Movie movie) {

        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getAverageRating(),
                movie.getDescription(),
                movie.getReleaseDate(),
                movie.getRuntime(),
                CategoryMapper.mapCategoryToCategoryDto(movie.getCategory()));
    }

    public static List<MovieDto> mapMovieListToMovieDtoList(List<Movie> movies) {
        return movies.stream().map(MovieMapper::mapMovieToMovieDto).toList();
    }
}
