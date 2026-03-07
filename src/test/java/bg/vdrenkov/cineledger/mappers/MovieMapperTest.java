package bg.vdrenkov.cineledger.mappers;

import org.junit.jupiter.api.extension.ExtendWith;
import bg.vdrenkov.cineledger.models.dtos.MovieDto;
import bg.vdrenkov.cineledger.testUtils.constants.MovieConstants;
import bg.vdrenkov.cineledger.testUtils.factories.CategoryFactory;
import bg.vdrenkov.cineledger.testUtils.factories.MovieFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieMapperTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private MovieMapper movieMapper;

    @Test
    public void testMapMovieToMovieDto_success() {
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(CategoryFactory.getDefaultCategoryDto());

        MovieDto movieDto = movieMapper.mapMovieToMovieDto(MovieFactory.getDefaultMovie());

        Assertions.assertEquals(MovieConstants.ID, movieDto.getId());
        Assertions.assertEquals(MovieConstants.TITLE, movieDto.getTitle());
        Assertions.assertEquals(MovieConstants.RATING, movieDto.getAverageRating(), 0.0);
        Assertions.assertEquals(MovieConstants.DESCRIPTION, movieDto.getDescription());
        assertTrue(MovieConstants.RELEASE_DATE.isEqual(movieDto.getReleaseDate()));
        Assertions.assertEquals(MovieConstants.RUNTIME, movieDto.getRuntime());
        assertTrue(Objects.nonNull(movieDto.getCategory()));
    }

    @Test
    public void testMapMovieListToMovieDtoList_success() {
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(CategoryFactory.getDefaultCategoryDto());

        List<MovieDto> movieDtos = movieMapper.mapMovieListToMovieDtoList(MovieFactory.getDefaultMovieList());
        MovieDto movieDto = movieDtos.get(0);

        Assertions.assertEquals(MovieConstants.ID, movieDto.getId());
        Assertions.assertEquals(MovieConstants.TITLE, movieDto.getTitle());
        Assertions.assertEquals(MovieConstants.RATING, movieDto.getAverageRating(), 0.0);
        Assertions.assertEquals(MovieConstants.DESCRIPTION, movieDto.getDescription());
        assertTrue(MovieConstants.RELEASE_DATE.isEqual(movieDto.getReleaseDate()));
        Assertions.assertEquals(MovieConstants.RUNTIME, movieDto.getRuntime());
        assertTrue(Objects.nonNull(movieDto.getCategory()));
    }
}



