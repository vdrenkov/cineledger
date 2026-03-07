package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.DateNotValidException;
import bg.vdrenkov.cineledger.exceptions.MovieAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.MovieNotFoundException;
import bg.vdrenkov.cineledger.mappers.MovieMapper;
import bg.vdrenkov.cineledger.models.dtos.MovieDto;
import bg.vdrenkov.cineledger.models.entities.Movie;
import bg.vdrenkov.cineledger.models.requests.MovieRequest;
import bg.vdrenkov.cineledger.repositories.MovieRepository;
import bg.vdrenkov.cineledger.testUtils.constants.MovieConstants;
import bg.vdrenkov.cineledger.testUtils.factories.CategoryFactory;
import bg.vdrenkov.cineledger.testUtils.factories.MovieFactory;
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
public class MovieServiceTest {

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

    @Test
    public void testGetMoviesByTitle_minRatingNotNull_success() {
        List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(
            movieRepository.findByTitleContainingAndAverageRatingGreaterThanEqual(anyString(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        List<MovieDto> resultList = movieService.getMoviesByTitle(MovieConstants.TITLE, MovieConstants.RATING);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetMoviesByTitle_minRatingNull_success() {
        List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByTitleContaining(anyString())).thenReturn(MovieFactory.getDefaultMovieList());

        List<MovieDto> resultList = movieService.getMoviesByTitle(MovieConstants.TITLE, 0);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetMoviesByCategory_minRatingNotNull_success() {
        List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByCategoryIdAndAverageRatingGreaterThanEqual(anyInt(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        List<MovieDto> resultList = movieService.getMoviesByCategory(MovieConstants.ID, MovieConstants.RATING);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetMoviesByCategory_minRatingNull_success() {
        List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByCategoryId(anyInt())).thenReturn(MovieFactory.getDefaultMovieList());

        List<MovieDto> resultList = movieService.getMoviesByCategory(MovieConstants.ID, 0);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetMoviesByReleaseDateAfter_isAfterTrue_minRatingNotNull_success() {
        List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByReleaseDateAfterAndAverageRatingGreaterThanEqual(any(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE,
            MovieConstants.RATING, true);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetMoviesByReleaseDateAfter_isAfterTrue_minRatingNull_success() {
        List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateAfter(any())).thenReturn(MovieFactory.getDefaultMovieList());
        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);

        List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE, null, true);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetMoviesByReleaseDateAfter_isAfterFalse_minRatingNull_success() {
        List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateBefore(any())).thenReturn(MovieFactory.getDefaultMovieList());
        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);

        List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE, null, false);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetMoviesByReleaseDateAfter_isAfterFalse_minRatingNotNull_success() {
        List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieRepository.findByReleaseDateBeforeAndAverageRatingGreaterThanEqual(any(), anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());
        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);

        List<MovieDto> resultList = movieService.getMoviesByReleaseDate(MovieConstants.RELEASE_DATE,
            MovieConstants.RATING, false);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetMoviesByMinRating_success() {
        List<MovieDto> expected = MovieFactory.getDefaultMovieDtoList();

        when(movieMapper.mapMovieListToMovieDtoList(any())).thenReturn(expected);
        when(movieRepository.findByAverageRatingGreaterThanEqual(anyDouble())).thenReturn(
            MovieFactory.getDefaultMovieList());

        List<MovieDto> resultList = movieService.getMoviesByMinRating(MovieConstants.RATING);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetImdbMoviesTest_success() {
        String filter = "top";
        String movies = "[{\"title\":\"title1\", \"year\":\"2001\"},{\"title\":\"title2\", \"year\":\"2002\"}]";

        when(imdbService.getMovies(filter)).thenReturn(movies);

        String result = movieService.getImdbMovies(filter);

        assertEquals(movies, result);
    }

    @Test
    public void testAddMovie_noExceptions_success() {
        Movie expected = MovieFactory.getDefaultMovie();

        when(movieRepository.save(any())).thenReturn(expected);
        when(categoryService.getCategoryById(anyInt())).thenReturn(CategoryFactory.getDefaultCategory());

        Movie movie = movieService.addMovie(MovieFactory.getDefaultMovieRequest());

        assertEquals(expected, movie);
    }

    @Test
    public void testAddMovie_throwsDateNotValidException() {
        assertThrows(DateNotValidException.class, () -> {

            MovieRequest request = MovieFactory.getDefaultMovieRequest();
            request.setReleaseDate(LocalDate.of(2000, 1, 1));

            movieService.addMovie(request);

        });
    }

    @Test
    public void testGetMoviesByTitle_WithEmptyTitle_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {

            String title = "";
            double minRating = 0.0;

            movieService.getMoviesByTitle(title, minRating);

        });
    }

    @Test
    public void testAddMovie_throwsExistingMovieException() {
        assertThrows(MovieAlreadyExistsException.class, () -> {

            MovieRequest request = MovieFactory.getDefaultMovieRequest();
            String existingTitle = "Existing Movie Title";
            request.setTitle(existingTitle);

            when(movieRepository.findByTitle(existingTitle)).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

            movieService.addMovie(request);

        });
    }

    @Test
    public void testUpdateMovie_success() {
        MovieDto expected = MovieFactory.getDefaultMovieDto();

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(expected);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(new Movie()));
        when(movieRepository.save(any())).thenReturn(MovieFactory.getDefaultMovie());

        MovieDto movie = movieService.updateMovie(MovieFactory.getDefaultMovieRequest(), MovieConstants.ID);

        assertEquals(expected, movie);
    }

    @Test
    public void testIsDateNotValid() {
        boolean result = movieService.isDateNotValid(LocalDate.of(2000, 1, 1));
        assertTrue(result);
    }

    @Test
    public void testUpdateMovie_throwsDateNotValidException() {
        assertThrows(DateNotValidException.class, () -> {

            MovieRequest request = MovieFactory.getDefaultMovieRequest();
            request.setReleaseDate(LocalDate.of(2000, 1, 1));

            when(movieMapper.mapMovieToMovieDto(any())).thenReturn(MovieFactory.getDefaultMovieDto());
            when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

            movieService.updateMovie(request, MovieConstants.ID);

        });
    }

    @Test
    public void testDeleteMovie_success() {
        MovieDto expected = MovieFactory.getDefaultMovieDto();

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(expected);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        MovieDto movie = movieService.deleteMovie(MovieConstants.ID);

        assertEquals(expected, movie);
    }

    @Test
    public void testGetMovieById_success() {
        Movie expected = MovieFactory.getDefaultMovie();

        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Movie movie = movieService.getMovieById(MovieConstants.ID);

        assertEquals(expected, movie);
    }

    @Test
    public void testGetMovieById_movieNotFound_throwsMovieNotFoundException() {
        assertThrows(MovieNotFoundException.class, () -> {

            when(movieRepository.findById(anyInt())).thenReturn(Optional.empty());

            movieService.getMovieById(MovieConstants.ID);

        });
    }

    @Test
    public void testGetMovieDtoById_success() {
        MovieDto expected = MovieFactory.getDefaultMovieDto();

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(expected);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        MovieDto movieDto = movieService.getMovieDtoById(MovieConstants.ID);

        assertEquals(expected, movieDto);
    }

    @Test
    public void testGetMovieByTitle_returnsMovie_success() {
        Movie expected = MovieFactory.getDefaultMovie();

        when(movieRepository.findByTitle(anyString())).thenReturn(Optional.of(expected));

        Movie result = movieService.getMovieByTitle(MovieConstants.TITLE);

        assertEquals(expected, result);
    }

    @Test
    public void testGetMovieByTitle_movieNotFound_throwsMovieNotFoundException() {
        assertThrows(MovieNotFoundException.class, () -> {

            when(movieRepository.findByTitle(anyString())).thenReturn(Optional.empty());

            movieService.getMovieByTitle(MovieConstants.TITLE);

        });
    }

    @Test
    public void testGetIdsOfMoviesByTitle_success() {
        when(movieRepository.findByTitleContaining(anyString())).thenReturn(MovieFactory.getDefaultMovieList());

        List<Integer> result = movieService.getIdsOfMoviesByTitle(MovieConstants.TITLE);

        assertEquals(1, result.size());
    }

    @Test
    public void testUpdateMovieAverageRating_success() {
        double newRating = 4.5;

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(MovieFactory.getDefaultMovieDto());
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(MovieFactory.getDefaultMovie()));

        MovieDto result = movieService.updateMovieAverageRating(newRating, MovieConstants.ID);

        assertEquals(newRating, result.getAverageRating(), 0.01);
    }
}



