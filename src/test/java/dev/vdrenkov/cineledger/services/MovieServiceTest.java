package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DateNotValidException;
import dev.vdrenkov.cineledger.exceptions.MovieAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.MovieNotFoundException;
import dev.vdrenkov.cineledger.mappers.MovieMapper;
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

/**
 * Tests movie service behavior.
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ImdbService imdbService;

    @InjectMocks
    private MovieService movieService;

    /**
     * Verifies that get Movies By Title min Rating Not Null success.
     */
    @Test
    void testGetMoviesByTitle_minRatingNotNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(
            movieRepository.findByTitleContainingAndAverageRatingGreaterThanEqual(anyString(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByTitle(MovieConstants.TITLE, MovieConstants.RATING);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Movies By Title min Rating Null success.
     */
    @Test
    void testGetMoviesByTitle_minRatingNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByTitleContaining(anyString())).thenReturn(MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByTitle(MovieConstants.TITLE, 0);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Movies By Category min Rating Not Null success.
     */
    @Test
    void testGetMoviesByCategory_minRatingNotNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByCategoryIdAndAverageRatingGreaterThanEqual(anyInt(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByCategory(MovieConstants.ID, MovieConstants.RATING);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Movies By Category min Rating Null success.
     */
    @Test
    void testGetMoviesByCategory_minRatingNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByCategoryId(anyInt())).thenReturn(MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByCategory(MovieConstants.ID, 0);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Movies By Release Date After is After True min Rating Not Null success.
     */
    @Test
    void testGetMoviesByReleaseDateAfter_isAfterTrue_minRatingNotNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByReleaseDateAfterAndAverageRatingGreaterThanEqual(any(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE,
            MovieConstants.RATING, true);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Movies By Release Date After is After True min Rating Null success.
     */
    @Test
    void testGetMoviesByReleaseDateAfter_isAfterTrue_minRatingNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateAfter(any())).thenReturn(MovieFactory.getDefaultMovieList());
        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);

        final List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE, null, true);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Movies By Release Date After is After False min Rating Null success.
     */
    @Test
    void testGetMoviesByReleaseDateAfter_isAfterFalse_minRatingNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateBefore(any())).thenReturn(MovieFactory.getDefaultMovieList());
        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);

        final List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE, null, false);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Movies By Release Date After is After False min Rating Not Null success.
     */
    @Test
    void testGetMoviesByReleaseDateAfter_isAfterFalse_minRatingNotNull_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateBeforeAndAverageRatingGreaterThanEqual(any(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());
        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);

        List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE,
            MovieConstants.RATING, false);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Movies By Min Rating success.
     */
    @Test
    void testGetMoviesByMinRating_success() {
        final List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByAverageRatingGreaterThanEqual(anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        final List<MovieDto> resultList = movieService.getMoviesByMinRating(MovieConstants.RATING);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get IMDb Movies Test success.
     */
    @Test
    void testGetImdbMoviesTest_success() {
        final String filter = "top";
        final String movies = "[{\"title\":\"title1\", \"year\":\"2001\"},{\"title\":\"title2\", \"year\":\"2002\"}]";

        when(imdbService.getMovies(filter)).thenReturn(movies);

        final String result = movieService.getImdbMovies(filter);

        assertEquals(movies, result);
    }

    /**
     * Verifies that add Movie no Exceptions success.
     */
    @Test
    void testAddMovie_noExceptions_success() {
        final Movie expected = MovieFactory.getDefaultMovie();

        when(movieRepository.save(any())).thenReturn(expected);
        when(categoryService.getCategoryById(anyInt())).thenReturn(CategoryFactory.getDefaultCategory());

        final Movie movie = movieService.addMovie(MovieFactory.getDefaultMovieRequest());

        assertEquals(expected, movie);
    }

    /**
     * Verifies that add Movie throws Date Not Valid Exception.
     */
    @Test
    void testAddMovie_throwsDateNotValidException() {
        assertThrows(DateNotValidException.class, () -> {

            final MovieRequest request = MovieFactory.getDefaultMovieRequest();
            request.setReleaseDate(LocalDate.of(2000, 1, 1));

            movieService.addMovie(request);

        });
    }

    /**
     * Verifies that get Movies By Title With Empty Title Should Throw Illegal Argument Exception.
     */
    @Test
    void testGetMoviesByTitle_WithEmptyTitle_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {

            final String title = "";
            final double minRating = 0.0;

            movieService.getMoviesByTitle(title, minRating);

        });
    }

    /**
     * Verifies that add Movie throws Existing Movie Exception.
     */
    @Test
    void testAddMovie_throwsExistingMovieException() {
        assertThrows(MovieAlreadyExistsException.class, () -> {

            final MovieRequest request = MovieFactory.getDefaultMovieRequest();
            final String existingTitle = "Existing Movie Title";
            request.setTitle(existingTitle);

            when(movieRepository.findByTitle(existingTitle)).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

            movieService.addMovie(request);

        });
    }

    /**
     * Verifies that update Movie success.
     */
    @Test
    void testUpdateMovie_success() {
        final MovieDto expected = MovieFactory.getDefaultMovieDto();

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(expected);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(new Movie()));
        when(movieRepository.save(any())).thenReturn(MovieFactory.getDefaultMovie());

        final MovieDto movie = movieService.updateMovie(MovieFactory.getDefaultMovieRequest(), MovieConstants.ID);

        assertEquals(expected, movie);
    }

    /**
     * Verifies that is Date Not Valid.
     */
    @Test
    void testIsDateNotValid() {
        final boolean result = movieService.isDateNotValid(LocalDate.of(2000, 1, 1));
        assertTrue(result);
    }

    /**
     * Verifies that update Movie throws Date Not Valid Exception.
     */
    @Test
    void testUpdateMovie_throwsDateNotValidException() {
        assertThrows(DateNotValidException.class, () -> {

            final MovieRequest request = MovieFactory.getDefaultMovieRequest();
            request.setReleaseDate(LocalDate.of(2000, 1, 1));

            when(movieMapper.mapMovieToMovieDto(any())).thenReturn(MovieFactory.getDefaultMovieDto());
            when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

            movieService.updateMovie(request, MovieConstants.ID);

        });
    }

    /**
     * Verifies that delete Movie success.
     */
    @Test
    void testDeleteMovie_success() {
        final MovieDto expected = MovieFactory.getDefaultMovieDto();

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(expected);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        final MovieDto movie = movieService.deleteMovie(MovieConstants.ID);

        assertEquals(expected, movie);
    }

    /**
     * Verifies that get Movie By Id success.
     */
    @Test
    void testGetMovieById_success() {
        final Movie expected = MovieFactory.getDefaultMovie();

        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Movie movie = movieService.getMovieById(MovieConstants.ID);

        assertEquals(expected, movie);
    }

    /**
     * Verifies that get Movie By Id movie Not Found throws Movie Not Found Exception.
     */
    @Test
    void testGetMovieById_movieNotFound_throwsMovieNotFoundException() {
        assertThrows(MovieNotFoundException.class, () -> {

            when(movieRepository.findById(anyInt())).thenReturn(Optional.empty());

            movieService.getMovieById(MovieConstants.ID);

        });
    }

    /**
     * Verifies that get Movie DTO By Id success.
     */
    @Test
    void testGetMovieDtoById_success() {
        final MovieDto expected = MovieFactory.getDefaultMovieDto();

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(expected);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        final MovieDto movieDto = movieService.getMovieDtoById(MovieConstants.ID);

        assertEquals(expected, movieDto);
    }

    /**
     * Verifies that get Movie By Title returns Movie success.
     */
    @Test
    void testGetMovieByTitle_returnsMovie_success() {
        final Movie expected = MovieFactory.getDefaultMovie();

        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(expected));

        final Movie result = movieService.getMovieByTitle(MovieConstants.TITLE);

        assertEquals(expected, result);
    }

    /**
     * Verifies that get Movie By Title movie Not Found throws Movie Not Found Exception.
     */
    @Test
    void testGetMovieByTitle_movieNotFound_throwsMovieNotFoundException() {
        assertThrows(MovieNotFoundException.class, () -> {

            when(movieRepository.findByTitle(anyString())).thenReturn(Optional.empty());

            movieService.getMovieByTitle(MovieConstants.TITLE);

        });
    }

    /**
     * Verifies that get Ids Of Movies By Title success.
     */
    @Test
    void testGetIdsOfMoviesByTitle_success() {
        when(movieRepository.findByTitleContaining(anyString())).thenReturn(MovieFactory.getDefaultMovieList());

        final List<Integer> result = movieService.getIdsOfMoviesByTitle(MovieConstants.TITLE);

        assertEquals(1, result.size());
    }

    /**
     * Verifies that update Movie Average Rating success.
     */
    @Test
    void testUpdateMovieAverageRating_success() {
        final double newRating = 4.5;

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(MovieFactory.getDefaultMovieDto());
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        final MovieDto result = movieService.updateMovieAverageRating(newRating, MovieConstants.ID);

        assertEquals(newRating, result.getAverageRating(), 0.01);
    }
}



