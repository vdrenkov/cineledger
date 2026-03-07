package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.services.MovieService;
import dev.vdrenkov.cineledger.testutils.factories.CategoryFactory;
import dev.vdrenkov.cineledger.testutils.factories.MovieFactory;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.DESCRIPTION;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.RATING;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.RELEASE_DATE;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.RUNTIME;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.TITLE;
import static dev.vdrenkov.cineledger.testutils.factories.MovieFactory.getDefaultMovieDto;
import static dev.vdrenkov.cineledger.testutils.factories.MovieFactory.getDefaultMovieDtoList;
import static dev.vdrenkov.cineledger.testutils.factories.MovieFactory.getDefaultMovieRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests movie controller behavior.
 */
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    private static final String RETURN_OLD = "returnOld";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    /**
     * Verifies that get Movies By Title success.
     */
    @Test
    void testGetMoviesByTitle_success() throws Exception {
        when(movieService.getMoviesByTitle(anyString(), anyDouble())).thenReturn(getDefaultMovieDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders
                .get(URIConstants.MOVIES_PATH)
                .queryParam("title", TITLE)
                .queryParam("minimalRating", String.valueOf(RATING)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].title").value(TITLE))
            .andExpect(jsonPath("$[0].averageRating").value(RATING))
            .andExpect(jsonPath("$[0].description").value(DESCRIPTION))
            .andExpect(jsonPath("$[0].releaseDate").value(RELEASE_DATE.toString()))
            .andExpect(jsonPath("$[0].runtime").value(RUNTIME.toString()))
            .andExpect(jsonPath("$[0].category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
            .andExpect(jsonPath("$[0].category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
    }

    /**
     * Verifies that get Movies By Category success.
     */
    @Test
    void testGetMoviesByCategory_success() throws Exception {
        final List<MovieDto> movieDtoList = getDefaultMovieDtoList();
        final MovieDto movieDto = movieDtoList.getFirst();
        when(movieService.getMoviesByCategory(anyInt(), anyDouble())).thenReturn(movieDtoList);
        mockMvc
            .perform(get(URIConstants.CATEGORIES_ID_MOVIES_PATH, ID).param("minRating", String.valueOf(RATING)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(movieDto.getId()))
            .andExpect(jsonPath("$[0].title").value(TITLE))
            .andExpect(jsonPath("$[0].averageRating").value(RATING))
            .andExpect(jsonPath("$[0].description").value(DESCRIPTION))
            .andExpect(jsonPath("$[0].releaseDate").value(RELEASE_DATE.toString()))
            .andExpect(jsonPath("$[0].runtime").value(RUNTIME.toString()))
            .andExpect(jsonPath("$[0].category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
            .andExpect(jsonPath("$[0].category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
    }

    /**
     * Verifies that get Movies By Release Date After success.
     */
    @Test
    void testGetMoviesByReleaseDateAfter_success() throws Exception {
        when(movieService.getMoviesByReleaseDate(any(), any(Double.class), anyBoolean())).thenReturn(
            Collections.singletonList(getDefaultMovieDto()));
        mockMvc
            .perform(MockMvcRequestBuilders
                .get(URIConstants.MOVIES_PATH)
                .param("release", String.valueOf(RELEASE_DATE))
                .param("rating", String.valueOf(RATING))
                .param("isAfter", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].title").value(TITLE))
            .andExpect(jsonPath("$[0].averageRating").value(RATING))
            .andExpect(jsonPath("$[0].description").value(DESCRIPTION))
            .andExpect(jsonPath("$[0].releaseDate").value(RELEASE_DATE.toString()))
            .andExpect(jsonPath("$[0].runtime").value(RUNTIME.toString()))
            .andExpect(jsonPath("$[0].category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
            .andExpect(jsonPath("$[0].category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
    }

    /**
     * Verifies that get Movies By Min Rating success.
     */
    @Test
    void testGetMoviesByMinRating_success() throws Exception {
        when(movieService.getMoviesByMinRating(anyDouble())).thenReturn(getDefaultMovieDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.MOVIES_PATH).param("minRating", String.valueOf(RATING)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].title").value(TITLE))
            .andExpect(jsonPath("$[0].averageRating").value(RATING))
            .andExpect(jsonPath("$[0].description").value(DESCRIPTION))
            .andExpect(jsonPath("$[0].releaseDate").value(RELEASE_DATE.toString()))
            .andExpect(jsonPath("$[0].runtime").value(RUNTIME.toString()))
            .andExpect(jsonPath("$[0].category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
            .andExpect(jsonPath("$[0].category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
    }

    /**
     * Verifies that add Movie success.
     */
    @Test
    void testAddMovie_success() throws Exception {
        when(movieService.addMovie(any())).thenReturn(MovieFactory.getDefaultMovie());

        final String json = objectMapper.writeValueAsString(getDefaultMovieRequest());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.MOVIES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.MOVIES_PATH + "/" + ID));
    }

    /**
     * Verifies that update Movie return Old True success.
     */
    @Test
    void testUpdateMovie_returnOldTrue_success() throws Exception {
        when(movieService.updateMovie(any(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

        final String json = objectMapper.writeValueAsString(getDefaultMovieRequest());

        mockMvc
            .perform(put(URIConstants.MOVIES_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.title").value(TITLE))
            .andExpect(jsonPath("$.averageRating").value(RATING))
            .andExpect(jsonPath("$.description").value(DESCRIPTION))
            .andExpect(jsonPath("$.releaseDate").value(RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.runtime").value(RUNTIME.toString()))
            .andExpect(jsonPath("$.category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
            .andExpect(jsonPath("$.category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
    }

    /**
     * Verifies that get Movies By IMDb Test.
     */
    @Test
    void testGetMoviesByImdbTest() throws Exception {
        final String imdb = "top";
        final String movies = "[{\"title\":\"title1\", \"year\":\"2001\"},{\"title\":\"title2\", \"year\":\"2002\"}]";

        when(movieService.getImdbMovies(imdb)).thenReturn(movies);

        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();

        mockMvc
            .perform(MockMvcRequestBuilders
                .get(URIConstants.MOVIES_PATH)
                .param("imdb", imdb)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(movies));
    }

    /**
     * Verifies that update Movie return Old False success.
     */
    @Test
    void testUpdateMovie_returnOldFalse_success() throws Exception {
        when(movieService.updateMovie(any(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

        final String json = objectMapper.writeValueAsString(getDefaultMovieRequest());

        mockMvc
            .perform(put(URIConstants.MOVIES_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam(RETURN_OLD, String.valueOf(false)))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete Movie return Old True success.
     */
    @Test
    void testDeleteMovie_returnOldTrue_success() throws Exception {
        when(movieService.deleteMovie(anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

        mockMvc
            .perform(delete(URIConstants.MOVIES_ID_PATH, ID).queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.title").value(TITLE))
            .andExpect(jsonPath("$.averageRating").value(RATING))
            .andExpect(jsonPath("$.description").value(DESCRIPTION))
            .andExpect(jsonPath("$.releaseDate").value(RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.runtime").value(RUNTIME.toString()))
            .andExpect(jsonPath("$.category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
            .andExpect(jsonPath("$.category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
    }

    /**
     * Verifies that delete Movie return Old False success.
     */
    @Test
    void testDeleteMovie_returnOldFalse_success() throws Exception {
        when(movieService.deleteMovie(anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

        mockMvc.perform(delete(URIConstants.MOVIES_ID_PATH, ID)).andExpect(status().isNoContent());
    }
}






