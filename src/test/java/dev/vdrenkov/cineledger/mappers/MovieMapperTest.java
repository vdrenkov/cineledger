package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.testutils.constants.MovieConstants;
import dev.vdrenkov.cineledger.testutils.factories.MovieFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests movie mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class MovieMapperTest {
    /**
     * Verifies that map Movie To Movie DTO success.
     */
    @Test
    void testMapMovieToMovieDto_success() {
        final MovieDto movieDto = MovieMapper.mapMovieToMovieDto(MovieFactory.getDefaultMovie());

        assertEquals(MovieConstants.ID, movieDto.getId());
        assertEquals(MovieConstants.TITLE, movieDto.getTitle());
        assertEquals(MovieConstants.RATING, movieDto.getAverageRating(), 0.0);
        assertEquals(MovieConstants.DESCRIPTION, movieDto.getDescription());
        assertTrue(MovieConstants.RELEASE_DATE.isEqual(movieDto.getReleaseDate()));
        assertEquals(MovieConstants.RUNTIME, movieDto.getRuntime());
        assertTrue(Objects.nonNull(movieDto.getCategory()));
    }

    /**
     * Verifies that map Movie List To Movie DTO List success.
     */
    @Test
    void testMapMovieListToMovieDtoList_success() {
        final List<MovieDto> movieDtos = MovieMapper.mapMovieListToMovieDtoList(MovieFactory.getDefaultMovieList());
        final MovieDto movieDto = movieDtos.getFirst();

        assertEquals(MovieConstants.ID, movieDto.getId());
        assertEquals(MovieConstants.TITLE, movieDto.getTitle());
        assertEquals(MovieConstants.RATING, movieDto.getAverageRating(), 0.0);
        assertEquals(MovieConstants.DESCRIPTION, movieDto.getDescription());
        assertTrue(MovieConstants.RELEASE_DATE.isEqual(movieDto.getReleaseDate()));
        assertEquals(MovieConstants.RUNTIME, movieDto.getRuntime());
        assertTrue(Objects.nonNull(movieDto.getCategory()));
    }
}



