package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.CinemaService;
import dev.vdrenkov.cineledger.testutil.factories.CinemaFactory;
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

import static dev.vdrenkov.cineledger.testutil.constants.CinemaConstants.ADDRESS;
import static dev.vdrenkov.cineledger.testutil.constants.CinemaConstants.CITY;
import static dev.vdrenkov.cineledger.testutil.constants.CinemaConstants.ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests cinema controller behavior.
 */
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CinemaControllerTest {
    private static final String RETURN_OLD = "returnOld";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private CinemaController cinemaController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cinemaController).build();
    }

    /**
     * Verifies that add Cinema cinema Added success.
     */
    @Test
    void testAddCinema_cinemaAdded_success() throws Exception {
        final String json = objectMapper.writeValueAsString(CinemaFactory.getDefaultCinemaRequest());
        when(cinemaService.addCinema(any())).thenReturn(CinemaFactory.getDefaultCinema());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.CINEMAS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.CINEMAS_PATH + "/" + ID));
    }

    /**
     * Verifies that get Cinemas get Cinema By City And Address success.
     */
    @Test
    void testGetCinemas_getCinemaByCityAndAddress_success() throws Exception {
        when(cinemaService.getAllCinemas(CITY, ADDRESS)).thenReturn(CinemaFactory.getDefaultCinemaDtoList());

        mockMvc
            .perform(
                MockMvcRequestBuilders.get(URIConstants.CINEMAS_PATH).param("city", CITY).param("address", ADDRESS))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].city").value(CITY))
            .andExpect(jsonPath("$[0].address").value(ADDRESS));
    }

    /**
     * Verifies that get Cinemas get Cinema By City success.
     */
    @Test
    void testGetCinemas_getCinemaByCity_success() throws Exception {
        when(cinemaService.getAllCinemas(CITY, null)).thenReturn(CinemaFactory.getDefaultCinemaDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.CINEMAS_PATH).param("city", CITY))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].city").value(CITY))
            .andExpect(jsonPath("$[0].address").value(ADDRESS));
    }

    /**
     * Verifies that get Cinemas get Cinema By Address success.
     */
    @Test
    void testGetCinemas_getCinemaByAddress_success() throws Exception {
        when(cinemaService.getAllCinemas(null, ADDRESS)).thenReturn(CinemaFactory.getDefaultCinemaDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.CINEMAS_PATH).param("address", ADDRESS))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].city").value(CITY))
            .andExpect(jsonPath("$[0].address").value(ADDRESS));
    }

    /**
     * Verifies that update Cinema return Old True success.
     */
    @Test
    void testUpdateCinema_returnOldTrue_success() throws Exception {
        when(cinemaService.updateCinema(any(), anyInt())).thenReturn(CinemaFactory.getDefaultCinemaDto());
        final String json = objectMapper.writeValueAsString(CinemaFactory.getDefaultCinemaRequest());

        mockMvc
            .perform(put(URIConstants.CINEMAS_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .param(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.city").value(CITY))
            .andExpect(jsonPath("$.address").value(ADDRESS));
    }

    /**
     * Verifies that update Cinema return Old False success.
     */
    @Test
    void testUpdateCinema_returnOldFalse_success() throws Exception {
        when(cinemaService.updateCinema(any(), anyInt())).thenReturn(CinemaFactory.getDefaultCinemaDto());
        final String json = objectMapper.writeValueAsString(CinemaFactory.getDefaultCinemaRequest());

        mockMvc
            .perform(put(URIConstants.CINEMAS_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete Cinema return Old True success.
     */
    @Test
    void testDeleteCinema_returnOldTrue_success() throws Exception {
        when(cinemaService.deleteCinema(anyInt())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        mockMvc
            .perform(delete(URIConstants.CINEMAS_ID_PATH, ID).param(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.city").value(CITY))
            .andExpect(jsonPath("$.address").value(ADDRESS));
    }

    /**
     * Verifies that delete Cinema return Old False success.
     */
    @Test
    void testDeleteCinema_returnOldFalse_success() throws Exception {
        when(cinemaService.deleteCinema(anyInt())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        mockMvc.perform(delete(URIConstants.CINEMAS_ID_PATH, ID)).andExpect(status().isNoContent());
    }
}





