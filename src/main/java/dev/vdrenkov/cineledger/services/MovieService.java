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
import dev.vdrenkov.cineledger.utils.constants.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contains business logic for movie operations.
 */
@Service
public class MovieService {
    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;
    private final CategoryService categoryService;
    private final ImdbService imdbService;

    /**
     * Creates a new movie service with its required collaborators.
     *
     * @param movieRepository
     *     movie repository used by the operation
     * @param categoryService
     *     category service used by the operation
     * @param imdbService
     *     imdb service used by the operation
     */
    @Autowired
    public MovieService(MovieRepository movieRepository, CategoryService categoryService, ImdbService imdbService) {
        this.movieRepository = movieRepository;
        this.categoryService = categoryService;
        this.imdbService = imdbService;
    }

    /**
     * Creates and persists movie.
     *
     * @param request
     *     request payload containing the submitted data
     * @return requested movie value
     */
    public Movie addMovie(MovieRequest request) {
        log.info("An attempt to add a new movie in the database");

        if (isDateNotValid(request.getReleaseDate())) {

            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.DATE_NOT_VALID_MESSAGE);

            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        this.movieRepository.findByTitle(request.getTitle()).ifPresent(movie -> {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE);

            throw new MovieAlreadyExistsException(ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE);
        });

        return movieRepository.save(
            new Movie(request.getTitle(), request.getDescription(), request.getReleaseDate(), request.getRuntime(),
                categoryService.getCategoryById(request.getCategoryId())));
    }

    /**
     * Returns movies matching the supplied criteria.
     *
     * @param title
     *     title text to search for
     * @param minRating
     *     minimum rating threshold
     * @return matching movie dto values
     */
    public List<MovieDto> getMoviesByTitle(String title, double minRating) {
        if (title == null || title.isEmpty()) {

            log.error(LogMessages.EXCEPTION_CAUGHT_LOG,
                "Required request parameter 'title' for method parameter type String is present but converted to null");

            throw new IllegalArgumentException(
                "Required request parameter 'title' for method parameter type String is present but converted to null");
        }

        if (minRating == 0.0) {
            log.info("All movies with title {} requested from the database", title);

            return MovieMapper.mapMovieListToMovieDtoList(movieRepository.findByTitleContaining(title));
        } else {
            log.info("All movies with title {} and rating {} requested from the database", title, minRating);

            return MovieMapper.mapMovieListToMovieDtoList(
                movieRepository.findByTitleContainingAndAverageRatingGreaterThanEqual(title, minRating));
        }
    }

    /**
     * Returns movies matching the supplied criteria.
     *
     * @param categoryId
     *     identifier of the target category
     * @param minRating
     *     minimum rating threshold
     * @return matching movie dto values
     */
    public List<MovieDto> getMoviesByCategory(int categoryId, double minRating) {
        List<MovieDto> movies;

        if (minRating == 0.0) {
            log.info("All movies with category id {} requested from the database", categoryId);

            movies = MovieMapper.mapMovieListToMovieDtoList(movieRepository.findByCategoryId(categoryId));
        } else {
            log.info("All movies with category id {} and rating {} requested from the database", categoryId, minRating);

            movies = MovieMapper.mapMovieListToMovieDtoList(
                movieRepository.findByCategoryIdAndAverageRatingGreaterThanEqual(categoryId, minRating));
        }

        movies = new ArrayList<>(movies);
        movies.sort(Comparator.comparing(MovieDto::getId));
        return movies;
    }

    /**
     * Returns movies matching the supplied criteria.
     *
     * @param releaseDate
     *     release date to validate or persist
     * @param minRating
     *     minimum rating threshold
     * @param isAfter
     *     whether to match data after the provided boundary
     * @return matching movie dto values
     */
    public List<MovieDto> getMoviesByReleaseDate(LocalDate releaseDate, Double minRating, boolean isAfter) {
        List<Movie> movies;

        log.info("All movies with releaseDate {} requested from the database", releaseDate);

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

    /**
     * Returns movies matching the supplied criteria.
     *
     * @param rating
     *     rating value to apply or filter by
     * @return matching movie dto values
     */
    public List<MovieDto> getMoviesByMinRating(double rating) {
        log.info("All movies with rating of {} requested from the database", rating);

        return MovieMapper.mapMovieListToMovieDtoList(movieRepository.findByAverageRatingGreaterThanEqual(rating));
    }

    /**
     * Returns movie matching the supplied criteria.
     *
     * @param title
     *     title text to search for
     * @return requested movie value
     */
    public Movie getMovieByTitle(String title) {
        return movieRepository.findByTitle(title).orElseThrow(() -> {

            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE);

            return new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns movie matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return movie dto result
     */
    public MovieDto getMovieDtoById(int id) {
        log.info("An attempt to extract a movie DTO with an id {} from the database", id);

        return MovieMapper.mapMovieToMovieDto(getMovieById(id));
    }

    /**
     * Returns movie matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested movie value
     */
    public Movie getMovieById(int id) {
        log.info("An attempt to extract a movie with an id {} from the database", id);

        return movieRepository.findById(id).orElseThrow(() -> {

            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE);

            return new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns ids of movies matching the supplied criteria.
     *
     * @param title
     *     title text to search for
     * @return matching integer values
     */
    public List<Integer> getIdsOfMoviesByTitle(String title) {
        log.info("All movies IDs with title {} requested from the database", title);

        final List<Movie> movies = movieRepository.findByTitleContaining(title);

        return movies.stream().map(Movie::getId).toList();
    }

    /**
     * Retrieves matching movies from the IMDb integration.
     *
     * @param filter
     *     external filter value
     * @return resulting string value
     */
    public String getImdbMovies(String filter) {
        return imdbService.getMovies(filter);
    }

    /**
     * Updates movie and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @return movie dto result
     */
    public MovieDto updateMovie(MovieRequest request, int id) {
        final MovieDto movieDto = getMovieDtoById(id);

        if (isDateNotValid(request.getReleaseDate())) {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.DATE_NOT_VALID_MESSAGE);

            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        movieRepository.save(
            new Movie(id, request.getTitle(), request.getDescription(), request.getReleaseDate(), request.getRuntime(),
                categoryService.getCategoryById(request.getCategoryId())));

        log.info("Movie with an id {} updated", id);

        return movieDto;
    }

    /**
     * Updates movie average rating and returns the previous state when needed.
     *
     * @param newRating
     *     replacement rating value
     * @param movieId
     *     identifier of the target movie
     * @return movie dto result
     */
    public MovieDto updateMovieAverageRating(double newRating, int movieId) {
        final Movie movie = getMovieById(movieId);

        final MovieDto movieDto = MovieMapper.mapMovieToMovieDto(movie);

        movie.setAverageRating(newRating);
        movieDto.setAverageRating(newRating);

        movieRepository.save(movie);

        log.info("Updated average rating for movie with id {}", movieId);

        return movieDto;
    }

    /**
     * Deletes movie and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @return movie dto result
     */
    public MovieDto deleteMovie(int id) {
        final MovieDto movieDto = getMovieDtoById(id);

        movieRepository.deleteById(id);

        log.info("Movie with an id {} deleted", id);

        return movieDto;
    }

    /**
     * Checks whether the supplied release date violates the application rule set.
     *
     * @param releaseDate
     *     release date to validate or persist
     * @return true when the requested condition holds; otherwise false
     */
    public static boolean isDateNotValid(LocalDate releaseDate) {
        return releaseDate.isBefore(LocalDate.now());
    }
}



