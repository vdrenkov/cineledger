package bg.vdrenkov.cineledger.controllers;

import bg.vdrenkov.cineledger.models.dtos.MovieDto;
import bg.vdrenkov.cineledger.services.MovieService;
import bg.vdrenkov.cineledger.testUtils.factories.CategoryFactory;
import bg.vdrenkov.cineledger.testUtils.factories.MovieFactory;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
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

import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.DESCRIPTION;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.RATING;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.RELEASE_DATE;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.RUNTIME;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.TITLE;
import static bg.vdrenkov.cineledger.testUtils.factories.MovieFactory.getDefaultMovieDto;
import static bg.vdrenkov.cineledger.testUtils.factories.MovieFactory.getDefaultMovieDtoList;
import static bg.vdrenkov.cineledger.testUtils.factories.MovieFactory.getDefaultMovieRequest;
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

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    private static final String RETURN_OLD = "returnOld";
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    public void testGetMoviesByTitle_success() throws Exception {
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

    @Test
    public void testGetMoviesByCategory_success() throws Exception {
        List<MovieDto> movieDtoList = getDefaultMovieDtoList();
        MovieDto movieDto = movieDtoList.get(0);
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

    @Test
    public void testGetMoviesByReleaseDateAfter_success() throws Exception {
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

    @Test
    public void testGetMoviesByMinRating_success() throws Exception {
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

    @Test
    public void testAddMovie_success() throws Exception {
        when(movieService.addMovie(any())).thenReturn(MovieFactory.getDefaultMovie());

        String json = objectMapper.writeValueAsString(getDefaultMovieRequest());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.MOVIES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.MOVIES_PATH + "/" + ID));
    }

    @Test
    public void testUpdateMovie_returnOldTrue_success() throws Exception {
        when(movieService.updateMovie(any(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

        String json = objectMapper.writeValueAsString(getDefaultMovieRequest());

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

    @Test
    public void testGetMoviesByImdbTest() throws Exception {
        String imdb = "top";
        String movies = "[{\"title\":\"title1\", \"year\":\"2001\"},{\"title\":\"title2\", \"year\":\"2002\"}]";

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

    @Test
    public void testUpdateMovie_returnOldFalse_success() throws Exception {
        when(movieService.updateMovie(any(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

        String json = objectMapper.writeValueAsString(getDefaultMovieRequest());

        mockMvc
            .perform(put(URIConstants.MOVIES_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam(RETURN_OLD, String.valueOf(false)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteMovie_returnOldTrue_success() throws Exception {
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

    @Test
    public void testDeleteMovie_returnOldFalse_success() throws Exception {
        when(movieService.deleteMovie(anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

        mockMvc.perform(delete(URIConstants.MOVIES_ID_PATH, ID)).andExpect(status().isNoContent());
    }
}






