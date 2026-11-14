package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DateNotValidException;
import dev.vdrenkov.cineledger.exceptions.MovieAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.MovieNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.requests.MovieRequest;
import dev.vdrenkov.cineledger.repositories.MovieRepository;
import dev.vdrenkov.cineledger.testutils.constants.MovieConstants;
import dev.vdrenkov.cineledger.testutils.factories.CategoryFactory;
import dev.vdrenkov.cineledger.testutils.factories.MovieFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private CategoryService categoryService;


    @InjectMocks
    private MovieService movieService;

    @Test
    void testGetMoviesByTitle_minRatingNotNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(
            movieRepository.findByTitleContainingAndAverageRatingGreaterThanEqual(anyString(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByTitle(MovieConstants.TITLE, MovieConstants.RATING);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetMoviesByTitle_minRatingNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByTitleContaining(anyString())).thenReturn(MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByTitle(MovieConstants.TITLE, 0);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetMoviesByCategory_minRatingNotNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByCategoryIdAndAverageRatingGreaterThanEqual(anyInt(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByCategory(MovieConstants.ID, MovieConstants.RATING);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetMoviesByCategory_minRatingNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByCategoryId(anyInt())).thenReturn(MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByCategory(MovieConstants.ID, 0);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetMoviesByReleaseDateAfter_isAfterTrue_minRatingNotNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateAfterAndAverageRatingGreaterThanEqual(any(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE,
            MovieConstants.RATING, true);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetMoviesByReleaseDateAfter_isAfterTrue_minRatingNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateAfter(any())).thenReturn(MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE, null, true);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetMoviesByReleaseDateAfter_isAfterFalse_minRatingNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateBefore(any())).thenReturn(MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE, null, false);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetMoviesByReleaseDateAfter_isAfterFalse_minRatingNotNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateBeforeAndAverageRatingGreaterThanEqual(any(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE,
            MovieConstants.RATING, false);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetMoviesByMinRating_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByAverageRatingGreaterThanEqual(anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByMinRating(MovieConstants.RATING);

        assertEquals(expected, resultList);
    }

    @Test
    void testAddMovie_noExceptions_success() {
        final Movie expected = MovieFactory.getDefaultMovie();

        when(movieRepository.save(any())).thenReturn(expected);
        when(categoryService.getCategoryById(anyInt())).thenReturn(CategoryFactory.getDefaultCategory());

        final Movie movie = movieService.addMovie(MovieFactory.getDefaultMovieRequest());

        assertEquals(expected, movie);
    }

    @Test
    void testAddMovie_throwsDateNotValidException() {
        final MovieRequest defaults = MovieFactory.getDefaultMovieRequest();
        final MovieRequest request = new MovieRequest(defaults.title(), defaults.description(), LocalDate.of(2000, 1, 1), defaults.runtime(), defaults.categoryId());

        assertThrows(DateNotValidException.class, () -> movieService.addMovie(request));
    }

    @Test
    void testGetMoviesByTitle_WithEmptyTitle_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {

            final String title = "";
            final double minRating = 0.0;

            movieService.getMoviesByTitle(title, minRating);

        });
    }

    @Test
    void testAddMovie_throwsExistingMovieException() {
        final MovieRequest defaults = MovieFactory.getDefaultMovieRequest();
        final String existingTitle = "Existing Movie Title";
        final MovieRequest request = new MovieRequest(existingTitle, defaults.description(), defaults.releaseDate(), defaults.runtime(), defaults.categoryId());
        when(movieRepository.findByTitle(existingTitle)).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        assertThrows(MovieAlreadyExistsException.class, () -> movieService.addMovie(request));
    }

    @Test
    void testUpdateMovie_success() {
        final MovieDto expected = MovieFactory.getDefaultMovieDto();

        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));
        when(categoryService.getCategoryById(anyInt())).thenReturn(CategoryFactory.getDefaultCategory());
        when(movieRepository.save(any())).thenReturn(MovieFactory.getDefaultMovie());

        final MovieDto movie = movieService.updateMovie(MovieFactory.getDefaultMovieRequest(), MovieConstants.ID);

        assertEquals(expected, movie);
    }

    @Test
    void testIsDateNotValid() {
        final boolean result = movieService.isDateNotValid(LocalDate.of(2000, 1, 1));
        assertTrue(result);
    }

    @Test
    void testUpdateMovie_throwsDateNotValidException() {
        final MovieRequest defaults = MovieFactory.getDefaultMovieRequest();
        final MovieRequest request = new MovieRequest(defaults.title(), defaults.description(), LocalDate.of(2000, 1, 1), defaults.runtime(), defaults.categoryId());

        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        assertThrows(DateNotValidException.class, () -> movieService.updateMovie(request, MovieConstants.ID));
    }

    @Test
    void testDeleteMovie_success() {
        final MovieDto expected = MovieFactory.getDefaultMovieDto();

        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        final MovieDto movie = movieService.deleteMovie(MovieConstants.ID);

        assertEquals(expected, movie);
    }

    @Test
    void testGetMovieById_success() {
        final Movie expected = MovieFactory.getDefaultMovie();

        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Movie movie = movieService.getMovieById(MovieConstants.ID);

        assertEquals(expected, movie);
    }

    @Test
    void testGetMovieById_movieNotFound_throwsMovieNotFoundException() {
        when(movieRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(MovieConstants.ID));
    }

    @Test
    void testGetMovieDtoById_success() {
        final MovieDto expected = MovieFactory.getDefaultMovieDto();

        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        final MovieDto movieDto = movieService.getMovieDtoById(MovieConstants.ID);

        assertEquals(expected, movieDto);
    }

    @Test
    void testGetMovieByTitle_returnsMovie_success() {
        final Movie expected = MovieFactory.getDefaultMovie();

        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(expected));

        final Movie result = movieService.getMovieByTitle(MovieConstants.TITLE);

        assertEquals(expected, result);
    }

    @Test
    void testGetMovieByTitle_movieNotFound_throwsMovieNotFoundException() {
        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieByTitle(MovieConstants.TITLE));
    }

    @Test
    void testGetIdsOfMoviesByTitle_success() {
        when(movieRepository.findByTitleContaining(anyString())).thenReturn(MovieFactory.getDefaultMovieList());

        final List<Integer> result = movieService.getIdsOfMoviesByTitle(MovieConstants.TITLE);

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateMovieAverageRating_success() {
        final double newRating = 4.5;

        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        final MovieDto result = movieService.updateMovieAverageRating(newRating, MovieConstants.ID);

        assertEquals(newRating, result.averageRating(), 0.01);
    }
}
