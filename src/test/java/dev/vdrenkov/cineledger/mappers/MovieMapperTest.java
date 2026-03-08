package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.testutil.constants.MovieConstants;
import dev.vdrenkov.cineledger.testutil.factories.CategoryFactory;
import dev.vdrenkov.cineledger.testutil.factories.MovieFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests movie mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class MovieMapperTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private MovieMapper movieMapper;

    /**
     * Verifies that map Movie To Movie DTO success.
     */
    @Test
    void testMapMovieToMovieDto_success() {
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(CategoryFactory.getDefaultCategoryDto());

        final MovieDto movieDto = movieMapper.mapMovieToMovieDto(MovieFactory.getDefaultMovie());

        Assertions.assertEquals(MovieConstants.ID, movieDto.getId());
        Assertions.assertEquals(MovieConstants.TITLE, movieDto.getTitle());
        Assertions.assertEquals(MovieConstants.RATING, movieDto.getAverageRating(), 0.0);
        Assertions.assertEquals(MovieConstants.DESCRIPTION, movieDto.getDescription());
        assertTrue(MovieConstants.RELEASE_DATE.isEqual(movieDto.getReleaseDate()));
        Assertions.assertEquals(MovieConstants.RUNTIME, movieDto.getRuntime());
        assertTrue(Objects.nonNull(movieDto.getCategory()));
    }

    /**
     * Verifies that map Movie List To Movie DTO List success.
     */
    @Test
    void testMapMovieListToMovieDtoList_success() {
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(CategoryFactory.getDefaultCategoryDto());

        final List<MovieDto> movieDtos = movieMapper.mapMovieListToMovieDtoList(MovieFactory.getDefaultMovieList());
        final MovieDto movieDto = movieDtos.getFirst();

        Assertions.assertEquals(MovieConstants.ID, movieDto.getId());
        Assertions.assertEquals(MovieConstants.TITLE, movieDto.getTitle());
        Assertions.assertEquals(MovieConstants.RATING, movieDto.getAverageRating(), 0.0);
        Assertions.assertEquals(MovieConstants.DESCRIPTION, movieDto.getDescription());
        assertTrue(MovieConstants.RELEASE_DATE.isEqual(movieDto.getReleaseDate()));
        Assertions.assertEquals(MovieConstants.RUNTIME, movieDto.getRuntime());
        assertTrue(Objects.nonNull(movieDto.getCategory()));
    }
}



