package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.requests.MovieRequest;
import dev.vdrenkov.cineledger.services.MovieService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
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

@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping(URIConstants.MOVIES_PATH)
    public ResponseEntity<Void> addMovie(@RequestBody @Valid MovieRequest movieRequest) {
        final Movie movie = movieService.addMovie(movieRequest);

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.MOVIES_ID_PATH)
            .buildAndExpand(movie.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = URIConstants.MOVIES_PATH, params = "title")
    public ResponseEntity<List<MovieDto>> getMoviesByTitle(@RequestParam String title,
        @RequestParam(required = false, defaultValue = "0.0") double minimalRating) {

        final List<MovieDto> movieDtoList = movieService.getMoviesByTitle(title, minimalRating);

        return ResponseEntity.ok(movieDtoList);
    }

    @GetMapping(value = URIConstants.MOVIES_PATH, params = "release")
    public ResponseEntity<List<MovieDto>> getMoviesByReleaseDate(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate release,
        @RequestParam(required = false) Double rating, @RequestParam(required = false) Boolean isAfter) {

        final List<MovieDto> movieDtoList = movieService.getMoviesByReleaseDate(release, rating, isAfter);

        return ResponseEntity.ok(movieDtoList);
    }

    @GetMapping(value = URIConstants.MOVIES_PATH, params = "minRating")
    public ResponseEntity<List<MovieDto>> getMoviesByMinRating(@RequestParam double minRating) {
        final List<MovieDto> movieDtoList = movieService.getMoviesByMinRating(minRating);

        return ResponseEntity.ok(movieDtoList);
    }

    @GetMapping(URIConstants.CATEGORIES_ID_MOVIES_PATH)
    public ResponseEntity<List<MovieDto>> getMoviesByCategory(@PathVariable("id") Integer categoryId,
        @RequestParam(required = false, defaultValue = "0.0") double minRating) {

        final List<MovieDto> movieDtoList = movieService.getMoviesByCategory(categoryId, minRating);

        return ResponseEntity.ok(movieDtoList);
    }

    @PutMapping(URIConstants.MOVIES_ID_PATH)
    public ResponseEntity<MovieDto> updateMovie(@RequestBody @Valid MovieRequest movieRequest, @PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final MovieDto movieDto = movieService.updateMovie(movieRequest, id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(movieDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.MOVIES_ID_PATH)
    public ResponseEntity<MovieDto> deleteMovie(@PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final MovieDto movieDto = movieService.deleteMovie(id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(movieDto) : ResponseEntity.noContent().build();
    }
}
