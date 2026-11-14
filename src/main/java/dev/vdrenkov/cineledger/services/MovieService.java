package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.MovieAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.MovieNotFoundException;
import dev.vdrenkov.cineledger.mappers.MovieMapper;
import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.requests.MovieRequest;
import dev.vdrenkov.cineledger.repositories.MovieRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final CategoryService categoryService;

    public MovieService(MovieRepository movieRepository, CategoryService categoryService) {
        this.movieRepository = movieRepository;
        this.categoryService = categoryService;
    }

    public Movie addMovie(MovieRequest request) {

        this.movieRepository
                .findByTitle(request.title())
                .ifPresent(movie -> {
                    throw new MovieAlreadyExistsException(
                            ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE);
                });

        return movieRepository.save(
                new Movie(
                        request.title(),
                        request.description(),
                        request.releaseDate(),
                        request.runtime(),
                        categoryService.getCategoryById(request.categoryId())));
    }

    public List<MovieDto> getMoviesByTitle(String title, double minRating) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException(
                    "Required request parameter 'title' for method parameter type String is present"
                            + " but converted to null");
        }

        if (minRating == 0.0) {
            return MovieMapper.mapMovieListToMovieDtoList(
                    movieRepository.findByTitleContaining(title));
        } else {

            return MovieMapper.mapMovieListToMovieDtoList(
                    movieRepository.findByTitleContainingAndAverageRatingGreaterThanEqual(
                            title, minRating));
        }
    }

    public List<MovieDto> getMoviesByCategory(int categoryId, double minRating) {
        List<MovieDto> movies;

        if (minRating == 0.0) {

            movies =
                    MovieMapper.mapMovieListToMovieDtoList(
                            movieRepository.findByCategoryId(categoryId));
        } else {

            movies =
                    MovieMapper.mapMovieListToMovieDtoList(
                            movieRepository.findByCategoryIdAndAverageRatingGreaterThanEqual(
                                    categoryId, minRating));
        }

        movies = new ArrayList<>(movies);
        movies.sort(Comparator.comparing(MovieDto::id));
        return movies;
    }

    public List<MovieDto> getMoviesByReleaseDate(
            LocalDate releaseDate, Double minRating, boolean isAfter) {
        List<Movie> movies;

        if (isAfter) {
            movies =
                    minRating != null
                            ? movieRepository
                                    .findByReleaseDateAfterAndAverageRatingGreaterThanEqual(
                                            releaseDate, minRating)
                            : movieRepository.findByReleaseDateAfter(releaseDate);
        } else {
            movies =
                    minRating != null
                            ? movieRepository
                                    .findByReleaseDateBeforeAndAverageRatingGreaterThanEqual(
                                            releaseDate, minRating)
                            : movieRepository.findByReleaseDateBefore(releaseDate);
        }

        return MovieMapper.mapMovieListToMovieDtoList(movies);
    }

    public List<MovieDto> getMoviesByMinRating(double rating) {

        return MovieMapper.mapMovieListToMovieDtoList(
                movieRepository.findByAverageRatingGreaterThanEqual(rating));
    }

    public Movie getMovieByTitle(String title) {
        return movieRepository
                .findByTitle(title)
                .orElseThrow(() -> {
                    return new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE);
                });
    }

    public MovieDto getMovieDtoById(int id) {

        return MovieMapper.mapMovieToMovieDto(getMovieById(id));
    }

    public Movie getMovieById(int id) {

        return movieRepository
                .findById(id)
                .orElseThrow(() -> {
                    return new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE);
                });
    }

    @Transactional
    public MovieDto updateMovie(MovieRequest request, int id) {
        final Movie stored = getMovieForUpdate(id);
        final MovieDto movieDto = MovieMapper.mapMovieToMovieDto(stored);
        stored.setTitle(request.title());
        stored.setDescription(request.description());
        stored.setReleaseDate(request.releaseDate());
        stored.setRuntime(request.runtime());
        stored.setCategory(categoryService.getCategoryById(request.categoryId()));
        movieRepository.save(stored);

        return movieDto;
    }

    public MovieDto updateMovieAverageRating(double newRating, int movieId) {
        final Movie movie = getMovieById(movieId);

        movie.setAverageRating(newRating);

        movieRepository.save(movie);

        return MovieMapper.mapMovieToMovieDto(movie);
    }

    public MovieDto deleteMovie(int id) {
        MovieDto movieDto = getMovieDtoById(id);

        movieRepository.deleteById(id);

        return movieDto;
    }

    public Movie getMovieForUpdate(final int id) {
        return movieRepository
                .findByIdForUpdate(id)
                .orElseThrow(() ->
                        new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE));
    }
}
