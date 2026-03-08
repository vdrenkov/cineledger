package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.ProgramService;
import dev.vdrenkov.cineledger.testutils.constants.CinemaConstants;
import dev.vdrenkov.cineledger.testutils.factories.ProgramFactory;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.format.DateTimeFormatter;

import static dev.vdrenkov.cineledger.testutils.constants.ProgramConstants.DATE;
import static dev.vdrenkov.cineledger.testutils.constants.ProgramConstants.ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests program controller behavior.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ProgramControllerTest {
    private static final String RETURN_OLD = "returnOld";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mvc;
    @Mock
    private ProgramService programService;
    @InjectMocks
    private ProgramController programController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(programController).build();
    }

    /**
     * Verifies that add Program program Added success.
     */
    @Test
    void testAddProgram_programAdded_success() throws Exception {
        when(programService.addProgram(any())).thenReturn(ProgramFactory.getDefaultProgram());

        final String json = objectMapper.writeValueAsString(ProgramFactory.getDefaultProgramRequest());

        mvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.LOCATION, URIConstants.PROGRAMS_PATH + "/" + ID));
    }

    /**
     * Verifies that get All Programs Test get By Program Date success.
     */
    @Test
    void testGetAllProgramsTest_getByProgramDate_success() throws Exception {
        when(programService.getAllPrograms(DATE)).thenReturn(ProgramFactory.getDefaultProgramDtoList());

        mvc
            .perform(MockMvcRequestBuilders
                .get(URIConstants.PROGRAMS_PATH)
                .param("date", DateTimeFormatter.ISO_LOCAL_DATE.format(DATE)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].programDate").value(DATE.toString()))
            .andExpect(jsonPath("$[0].cinema.id").value(CinemaConstants.ID))
            .andExpect(jsonPath("$[0].cinema.city").value(CinemaConstants.CITY))
            .andExpect(jsonPath("$[0].cinema.address").value(CinemaConstants.ADDRESS));
    }

    /**
     * Verifies that get All Programs Test get A Ll Programs success.
     */
    @Test
    void testGetAllProgramsTest_getALlPrograms_success() throws Exception {
        when(programService.getAllPrograms(null)).thenReturn(ProgramFactory.getDefaultProgramDtoList());

        mvc
            .perform(MockMvcRequestBuilders.get(URIConstants.PROGRAMS_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].programDate").value(DATE.toString()))
            .andExpect(jsonPath("$[0].cinema.id").value(CinemaConstants.ID))
            .andExpect(jsonPath("$[0].cinema.city").value(CinemaConstants.CITY))
            .andExpect(jsonPath("$[0].cinema.address").value(CinemaConstants.ADDRESS));
    }

    /**
     * Verifies that get Programs By Cinema Id success.
     */
    @Test
    void testGetProgramsByCinemaId_success() throws Exception {
        when(programService.getProgramsByCinemaId(CinemaConstants.ID)).thenReturn(
            ProgramFactory.getDefaultProgramDtoList());

        mvc
            .perform(get(URIConstants.CINEMAS_ID_PROGRAMS_PATH, CinemaConstants.ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].programDate").value(DATE.toString()))
            .andExpect(jsonPath("$[0].cinema.id").value(CinemaConstants.ID))
            .andExpect(jsonPath("$[0].cinema.city").value(CinemaConstants.CITY))
            .andExpect(jsonPath("$[0].cinema.address").value(CinemaConstants.ADDRESS));
    }

    /**
     * Verifies that update Program return Old True success.
     */
    @Test
    void testUpdateProgram_returnOldTrue_success() throws Exception {
        when(programService.updateProgram(any(), anyInt())).thenReturn(ProgramFactory.getDefaultProgramDto());

        final String json = objectMapper.writeValueAsString(ProgramFactory.getDefaultProgramRequest());

        mvc
            .perform(put(URIConstants.PROGRAMS_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .param(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.programDate").value(DATE.toString()))
            .andExpect(jsonPath("$.cinema.id").value(CinemaConstants.ID))
            .andExpect(jsonPath("$.cinema.city").value(CinemaConstants.CITY))
            .andExpect(jsonPath("$.cinema.address").value(CinemaConstants.ADDRESS));
    }

    /**
     * Verifies that update Program return Old False success.
     */
    @Test
    void testUpdateProgram_returnOldFalse_success() throws Exception {
        when(programService.updateProgram(any(), anyInt())).thenReturn(ProgramFactory.getDefaultProgramDto());

        final String json = objectMapper.writeValueAsString(ProgramFactory.getDefaultProgramRequest());

        mvc
            .perform(put(URIConstants.PROGRAMS_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete Program return Old True success.
     */
    @Test
    void testDeleteProgram_returnOldTrue_success() throws Exception {
        when(programService.deleteProgram(anyInt())).thenReturn(ProgramFactory.getDefaultProgramDto());

        mvc
            .perform(delete(URIConstants.PROGRAMS_ID_PATH, ID).param(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.programDate").value(DATE.toString()))
            .andExpect(jsonPath("$.cinema.id").value(CinemaConstants.ID))
            .andExpect(jsonPath("$.cinema.city").value(CinemaConstants.CITY))
            .andExpect(jsonPath("$.cinema.address").value(CinemaConstants.ADDRESS));
    }

    /**
     * Verifies that delete Program return Old False success.
     */
    @Test
    void testDeleteProgram_returnOldFalse_success() throws Exception {
        when(programService.deleteProgram(anyInt())).thenReturn(ProgramFactory.getDefaultProgramDto());
        mvc.perform(delete(URIConstants.PROGRAMS_ID_PATH, ID)).andExpect(status().isNoContent());
    }
}







