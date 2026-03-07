package bg.vdrenkov.cineledger.controllers;

import bg.vdrenkov.cineledger.services.ProgramService;
import bg.vdrenkov.cineledger.testUtils.constants.CinemaConstants;
import bg.vdrenkov.cineledger.testUtils.factories.ProgramFactory;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
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

import static bg.vdrenkov.cineledger.testUtils.constants.ProgramConstants.DATE;
import static bg.vdrenkov.cineledger.testUtils.constants.ProgramConstants.ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ProgramControllerTest {

    private static final String RETURN_OLD = "returnOld";
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mvc;
    @Mock
    private ProgramService programService;
    @InjectMocks
    private ProgramController programController;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(programController).build();
    }

    @Test
    public void testAddProgram_programAdded_success() throws Exception {
        when(programService.addProgram(any())).thenReturn(ProgramFactory.getDefaultProgram());

        String json = objectMapper.writeValueAsString(ProgramFactory.getDefaultProgramRequest());

        mvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.LOCATION, URIConstants.PROGRAMS_PATH + "/" + ID));
    }

    @Test
    public void testGetAllProgramsTest_getByProgramDate_success() throws Exception {
        when(programService.getAllPrograms(eq(DATE))).thenReturn(ProgramFactory.getDefaultProgramDtoList());

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

    @Test

    public void testGetAllProgramsTest_getALlPrograms_success() throws Exception {
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

    @Test
    public void testGetProgramsByCinemaId_success() throws Exception {
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

    @Test
    public void testUpdateProgram_returnOldTrue_success() throws Exception {
        when(programService.updateProgram(any(), anyInt())).thenReturn(ProgramFactory.getDefaultProgramDto());

        String json = objectMapper.writeValueAsString(ProgramFactory.getDefaultProgramRequest());

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

    @Test
    public void testUpdateProgram_returnOldFalse_success() throws Exception {
        when(programService.updateProgram(any(), anyInt())).thenReturn(ProgramFactory.getDefaultProgramDto());

        String json = objectMapper.writeValueAsString(ProgramFactory.getDefaultProgramRequest());

        mvc
            .perform(put(URIConstants.PROGRAMS_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProgram_returnOldTrue_success() throws Exception {
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

    @Test
    public void testDeleteProgram_returnOldFalse_success() throws Exception {
        when(programService.deleteProgram(anyInt())).thenReturn(ProgramFactory.getDefaultProgramDto());
        mvc.perform(delete(URIConstants.PROGRAMS_ID_PATH, ID)).andExpect(status().isNoContent());
    }
}







