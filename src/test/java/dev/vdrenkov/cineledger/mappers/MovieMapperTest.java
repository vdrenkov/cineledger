package dev.vdrenkov.cineledger.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.testutils.constants.MovieConstants;
import dev.vdrenkov.cineledger.testutils.factories.MovieFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class MovieMapperTest {

    @Test
    void testMapMovieToMovieDto_success() {
        final MovieDto movieDto = MovieMapper.mapMovieToMovieDto(MovieFactory.getDefaultMovie());

        assertEquals(MovieConstants.ID, movieDto.id());
        assertEquals(MovieConstants.TITLE, movieDto.title());
        assertEquals(MovieConstants.RATING, movieDto.averageRating(), 0.0);
        assertEquals(MovieConstants.DESCRIPTION, movieDto.description());
        assertTrue(MovieConstants.RELEASE_DATE.isEqual(movieDto.releaseDate()));
        assertEquals(MovieConstants.RUNTIME, movieDto.runtime());
        assertTrue(Objects.nonNull(movieDto.category()));
    }

    @Test
    void testMapMovieListToMovieDtoList_success() {
        final List<MovieDto> movieDtos =
                MovieMapper.mapMovieListToMovieDtoList(MovieFactory.getDefaultMovieList());
        final MovieDto movieDto = movieDtos.getFirst();

        assertEquals(MovieConstants.ID, movieDto.id());
        assertEquals(MovieConstants.TITLE, movieDto.title());
        assertEquals(MovieConstants.RATING, movieDto.averageRating(), 0.0);
        assertEquals(MovieConstants.DESCRIPTION, movieDto.description());
        assertTrue(MovieConstants.RELEASE_DATE.isEqual(movieDto.releaseDate()));
        assertEquals(MovieConstants.RUNTIME, movieDto.runtime());
        assertTrue(Objects.nonNull(movieDto.category()));
    }
}
