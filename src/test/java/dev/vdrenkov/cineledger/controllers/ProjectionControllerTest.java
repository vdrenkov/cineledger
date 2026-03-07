package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.ProjectionService;
import dev.vdrenkov.cineledger.testUtils.constants.MovieConstants;
import dev.vdrenkov.cineledger.testUtils.constants.ProgramConstants;
import dev.vdrenkov.cineledger.testUtils.factories.CategoryFactory;
import dev.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testUtils.factories.HallFactory;
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

import static dev.vdrenkov.cineledger.testUtils.constants.ProjectionConstants.ID;
import static dev.vdrenkov.cineledger.testUtils.constants.ProjectionConstants.PRICE;
import static dev.vdrenkov.cineledger.testUtils.constants.ProjectionConstants.START_TIME;
import static dev.vdrenkov.cineledger.testUtils.factories.ProjectionFactory.getDefaultProjection;
import static dev.vdrenkov.cineledger.testUtils.factories.ProjectionFactory.getDefaultProjectionDto;
import static dev.vdrenkov.cineledger.testUtils.factories.ProjectionFactory.getDefaultProjectionDtoList;
import static dev.vdrenkov.cineledger.testUtils.factories.ProjectionFactory.getDefaultProjectionRequest;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ProjectionControllerTest {

    private static final String START_TIME_STRING = "startTime";
    private static final String RETURN_OLD = "returnOld";
    private static final String IS_BEFORE = "isBefore";

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private ProjectionService projectionService;

    @InjectMocks
    private ProjectionController projectionController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectionController).build();
    }

    @Test
    public void testAddProjection_projectionAdded_success() throws Exception {
        when(projectionService.addProjection(any())).thenReturn(getDefaultProjection());

        String json = objectMapper.writeValueAsString(getDefaultProjectionRequest());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.PROJECTIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.PROJECTIONS_PATH + "/" + ID));
    }

    @Test
    public void testGetProjectionsByProgramId_noExceptions_success() throws Exception {
        when(projectionService.getProjectionsByProgramId(anyInt())).thenReturn(getDefaultProjectionDtoList());

        mockMvc
            .perform(get(URIConstants.PROGRAMS_ID_PROJECTIONS_PATH, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].hall").value(HallFactory.getDefaultHallDto()))
            .andExpect(jsonPath("$[0].price").value(PRICE))
            .andExpect(jsonPath("$[0].program.id").value(ProgramConstants.ID))
            .andExpect(jsonPath("$[0].program.programDate", is(ProgramConstants.DATE.toString())))
            .andExpect(jsonPath("$[0].program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$[0].movie.averageRating").value(MovieConstants.RATING))
            .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$[0].movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$[0].movie.runtime").value(MovieConstants.RUNTIME.toString()))
            .andExpect(jsonPath("$[0].movie.category").value(CategoryFactory.getDefaultCategoryDto()))
            .andExpect(jsonPath("$[0].startTime", is(START_TIME.toString())));
    }

    @Test
    public void testGetProjectionsByMovieId_noExceptions_success() throws Exception {
        when(projectionService.getProjectionsByMovieId(anyInt())).thenReturn(getDefaultProjectionDtoList());

        mockMvc
            .perform(get(URIConstants.MOVIES_ID_PROJECTIONS_PATH, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].hall").value(HallFactory.getDefaultHallDto()))
            .andExpect(jsonPath("$[0].price").value(PRICE))
            .andExpect(jsonPath("$[0].program.id").value(ProgramConstants.ID))
            .andExpect(jsonPath("$[0].program.programDate", is(ProgramConstants.DATE.toString())))
            .andExpect(jsonPath("$[0].program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$[0].movie.averageRating").value(MovieConstants.RATING))
            .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$[0].movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$[0].movie.runtime").value(MovieConstants.RUNTIME.toString()))
            .andExpect(jsonPath("$[0].movie.category").value(CategoryFactory.getDefaultCategoryDto()))
            .andExpect(jsonPath("$[0].startTime", is(START_TIME.toString())));
    }

    @Test
    public void testGetProjectionsByStartTime_returnsProjectionsList_success() throws Exception {
        when(projectionService.getProjectionsByStartTime(any(), anyBoolean())).thenReturn(
            getDefaultProjectionDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders
                .get(URIConstants.PROJECTIONS_PATH)
                .queryParam(START_TIME_STRING, String.valueOf(START_TIME))
                .queryParam(IS_BEFORE, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].hall").value(HallFactory.getDefaultHallDto()))
            .andExpect(jsonPath("$[0].price").value(PRICE))
            .andExpect(jsonPath("$[0].program.id").value(ProgramConstants.ID))
            .andExpect(jsonPath("$[0].program.programDate", is(ProgramConstants.DATE.toString())))
            .andExpect(jsonPath("$[0].program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$[0].movie.averageRating").value(MovieConstants.RATING))
            .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$[0].movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$[0].movie.runtime").value(MovieConstants.RUNTIME.toString()))
            .andExpect(jsonPath("$[0].movie.category").value(CategoryFactory.getDefaultCategoryDto()))
            .andExpect(jsonPath("$[0].startTime", is(START_TIME.toString())));
    }

    @Test
    public void testUpdateProjection_returnOldTrue_success() throws Exception {
        when(projectionService.updateProjection(any(), anyInt())).thenReturn(getDefaultProjectionDto());

        String json = objectMapper.writeValueAsString(getDefaultProjectionRequest());

        mockMvc
            .perform(put(URIConstants.PROJECTIONS_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.hall").value(HallFactory.getDefaultHallDto()))
            .andExpect(jsonPath("$.price").value(PRICE))
            .andExpect(jsonPath("$.program.id").value(ProgramConstants.ID))
            .andExpect(jsonPath("$.program.programDate", is(ProgramConstants.DATE.toString())))
            .andExpect(jsonPath("$.program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$.movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$.movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$.movie.averageRating").value(MovieConstants.RATING))
            .andExpect(jsonPath("$.movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$.movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.movie.runtime").value(MovieConstants.RUNTIME.toString()))
            .andExpect(jsonPath("$.movie.category").value(CategoryFactory.getDefaultCategoryDto()))
            .andExpect(jsonPath("$.startTime", is(START_TIME.toString())));
    }

    @Test
    public void testUpdateProjection_returnOldFalse_success() throws Exception {
        when(projectionService.updateProjection(any(), anyInt())).thenReturn(getDefaultProjectionDto());

        String json = objectMapper.writeValueAsString(getDefaultProjectionRequest());

        mockMvc
            .perform(put(URIConstants.PROJECTIONS_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam(RETURN_OLD, String.valueOf(false)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProjection_returnOldTrue_success() throws Exception {
        when(projectionService.deleteProjection(anyInt())).thenReturn(getDefaultProjectionDto());

        mockMvc
            .perform(delete(URIConstants.PROJECTIONS_ID_PATH, ID).queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.hall").value(HallFactory.getDefaultHallDto()))
            .andExpect(jsonPath("$.price").value(PRICE))
            .andExpect(jsonPath("$.program.id").value(ProgramConstants.ID))
            .andExpect(jsonPath("$.program.programDate", is(ProgramConstants.DATE.toString())))
            .andExpect(jsonPath("$.program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$.movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$.movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$.movie.averageRating").value(MovieConstants.RATING))
            .andExpect(jsonPath("$.movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$.movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.movie.runtime").value(MovieConstants.RUNTIME.toString()))
            .andExpect(jsonPath("$.movie.category").value(CategoryFactory.getDefaultCategoryDto()))
            .andExpect(jsonPath("$.startTime", is(START_TIME.toString())));
    }

    @Test
    public void testDeleteProjection_returnOldFalse_success() throws Exception {
        when(projectionService.deleteProjection(anyInt())).thenReturn(getDefaultProjectionDto());

        mockMvc.perform(delete(URIConstants.PROJECTIONS_ID_PATH, ID)).andExpect(status().isNoContent());
    }
}






