package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.requests.MovieRequest;
import dev.vdrenkov.cineledger.services.MovieService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 * Exposes REST endpoints for managing movie data.
 */
@RestController
public class MovieController {
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;

    /**
     * Creates a new movie controller with its required collaborators.
     *
     * @param movieService
     *     movie service used by the operation
     */
    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Creates and persists movie.
     *
     * @param movieRequest
     *     request payload for the movie operation
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.MOVIES_PATH)
    public ResponseEntity<Void> addMovie(@RequestBody @Valid MovieRequest movieRequest) {
        final Movie movie = movieService.addMovie(movieRequest);
        log.info("Request for a movie to be added submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.MOVIES_ID_PATH)
            .buildAndExpand(movie.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Returns movies matching the supplied criteria.
     *
     * @param title
     *     title text to search for
     * @param minimalRating
     *     minimum rating threshold
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.MOVIES_PATH, params = "title")
    public ResponseEntity<List<MovieDto>> getMoviesByTitle(@RequestParam String title,
        @RequestParam(required = false, defaultValue = "0.0") double minimalRating) {

        final List<MovieDto> movieDtoList = movieService.getMoviesByTitle(title, minimalRating);
        log.info("Movies by title requested from the database");

        return ResponseEntity.ok(movieDtoList);
    }

    /**
     * Returns movies matching the supplied criteria.
     *
     * @param release
     *     release date to filter by
     * @param rating
     *     rating value to apply or filter by
     * @param isAfter
     *     whether to match data after the provided boundary
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.MOVIES_PATH, params = "release")
    public ResponseEntity<List<MovieDto>> getMoviesByReleaseDate(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate release,
        @RequestParam(required = false) Double rating, @RequestParam(required = false) Boolean isAfter) {

        final List<MovieDto> movieDtoList = movieService.getMoviesByReleaseDate(release, rating, isAfter);
        log.info("Movies by release date after requested from the database");

        return ResponseEntity.ok(movieDtoList);
    }

    /**
     * Returns movies matching the supplied criteria.
     *
     * @param minRating
     *     minimum rating threshold
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.MOVIES_PATH, params = "minRating")
    public ResponseEntity<List<MovieDto>> getMoviesByMinRating(@RequestParam double minRating) {
        final List<MovieDto> movieDtoList = movieService.getMoviesByMinRating(minRating);
        log.info("Movies by minimum rating requested from the database");

        return ResponseEntity.ok(movieDtoList);
    }

    /**
     * Returns movies matching the supplied criteria.
     *
     * @param imdb
     *     imdb used by the operation
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.MOVIES_PATH, params = "imdb")
    public ResponseEntity<String> getMoviesByImdb(@RequestParam String imdb) {
        final String movies = movieService.getImdbMovies(imdb);
        log.info("IMDB movies requested from the database");

        return ResponseEntity.ok(movies);
    }

    /**
     * Returns movies matching the supplied criteria.
     *
     * @param categoryId
     *     identifier of the target category
     * @param minRating
     *     minimum rating threshold
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.CATEGORIES_ID_MOVIES_PATH)
    public ResponseEntity<List<MovieDto>> getMoviesByCategory(@PathVariable("id") Integer categoryId,
        @RequestParam(required = false, defaultValue = "0.0") double minRating) {

        final List<MovieDto> movieDtoList = movieService.getMoviesByCategory(categoryId, minRating);
        log.info("Movies by category requested from the database");

        return ResponseEntity.ok(movieDtoList);
    }

    /**
     * Updates movie and returns the previous state when needed.
     *
     * @param movieRequest
     *     request payload for the movie operation
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.MOVIES_ID_PATH)
    public ResponseEntity<MovieDto> updateMovie(@RequestBody @Valid MovieRequest movieRequest, @PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final MovieDto movieDto = movieService.updateMovie(movieRequest, id);
        log.info("Movie with id {} updated", id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(movieDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes movie and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(URIConstants.MOVIES_ID_PATH)
    public ResponseEntity<MovieDto> deleteMovie(@PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final MovieDto movieDto = movieService.deleteMovie(id);
        log.info("Movie with id {} deleted", id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(movieDto) : ResponseEntity.noContent().build();
    }
}



