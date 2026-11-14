package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DateNotValidException;
import dev.vdrenkov.cineledger.exceptions.MovieAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.MovieNotFoundException;
import dev.vdrenkov.cineledger.mappers.MovieMapper;
import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.requests.MovieRequest;
import dev.vdrenkov.cineledger.repositories.MovieRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final CategoryService categoryService;
    private final ImdbService imdbService;

    public MovieService(MovieRepository movieRepository, CategoryService categoryService, ImdbService imdbService) {
        this.movieRepository = movieRepository;
        this.categoryService = categoryService;
        this.imdbService = imdbService;
    }

    public Movie addMovie(MovieRequest request) {

        if (isDateNotValid(request.releaseDate())) {

            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        this.movieRepository.findByTitle(request.title()).ifPresent(movie -> {

            throw new MovieAlreadyExistsException(ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE);
        });

        return movieRepository.save(
            new Movie(request.title(), request.description(), request.releaseDate(), request.runtime(),
                categoryService.getCategoryById(request.categoryId())));
    }

    public List<MovieDto> getMoviesByTitle(String title, double minRating) {
        if (title == null || title.isEmpty()) {

            throw new IllegalArgumentException(
                "Required request parameter 'title' for method parameter type String is present but converted to null");
        }

        if (minRating == 0.0) {

            return MovieMapper.mapMovieListToMovieDtoList(movieRepository.findByTitleContaining(title));
        } else {

            return MovieMapper.mapMovieListToMovieDtoList(
                movieRepository.findByTitleContainingAndAverageRatingGreaterThanEqual(title, minRating));
        }
    }

    public List<MovieDto> getMoviesByCategory(int categoryId, double minRating) {
        List<MovieDto> movies;

        if (minRating == 0.0) {

            movies = MovieMapper.mapMovieListToMovieDtoList(movieRepository.findByCategoryId(categoryId));
        } else {

            movies = MovieMapper.mapMovieListToMovieDtoList(
                movieRepository.findByCategoryIdAndAverageRatingGreaterThanEqual(categoryId, minRating));
        }

        movies = new ArrayList<>(movies);
        movies.sort(Comparator.comparing(MovieDto::id));
        return movies;
    }

    public List<MovieDto> getMoviesByReleaseDate(LocalDate releaseDate, Double minRating, boolean isAfter) {
        List<Movie> movies;

        if (isAfter) {
            movies = minRating != null ?
                movieRepository.findByReleaseDateAfterAndAverageRatingGreaterThanEqual(releaseDate, minRating) :
                movieRepository.findByReleaseDateAfter(releaseDate);
        } else {
            movies = minRating != null ?
                movieRepository.findByReleaseDateBeforeAndAverageRatingGreaterThanEqual(releaseDate, minRating) :
                movieRepository.findByReleaseDateBefore(releaseDate);
        }

        return MovieMapper.mapMovieListToMovieDtoList(movies);
    }

    public List<MovieDto> getMoviesByMinRating(double rating) {

        return MovieMapper.mapMovieListToMovieDtoList(movieRepository.findByAverageRatingGreaterThanEqual(rating));
    }

    public Movie getMovieByTitle(String title) {
        return movieRepository.findByTitle(title).orElseThrow(() -> {

            return new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE);
        });
    }

    public MovieDto getMovieDtoById(int id) {

        return MovieMapper.mapMovieToMovieDto(getMovieById(id));
    }

    public Movie getMovieById(int id) {

        return movieRepository.findById(id).orElseThrow(() -> {

            return new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE);
        });
    }

    public List<Integer> getIdsOfMoviesByTitle(String title) {

        final List<Movie> movies = movieRepository.findByTitleContaining(title);

        return movies.stream().map(Movie::getId).toList();
    }

    public String getImdbMovies(String filter) {
        return imdbService.getMovies(filter);
    }

    public MovieDto updateMovie(MovieRequest request, int id) {
        MovieDto movieDto = getMovieDtoById(id);

        if (isDateNotValid(request.releaseDate())) {

            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        movieRepository.save(
            new Movie(id, request.title(), request.description(), request.releaseDate(), request.runtime(),
                categoryService.getCategoryById(request.categoryId())));

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

    public static boolean isDateNotValid(LocalDate releaseDate) {
        return releaseDate.isBefore(LocalDate.now());
    }
}
