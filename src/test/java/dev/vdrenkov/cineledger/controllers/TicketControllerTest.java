package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.services.TicketService;
import dev.vdrenkov.cineledger.testutils.factories.TicketFactory;
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

import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.TicketConstants.DATE_OF_PURCHASE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests ticket controller behavior.
 */
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TicketControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    /**
     * Verifies that add Ticket ticket Added success.
     */
    @Test
    void testAddTicket_ticketAdded_success() throws Exception {
        final String json = objectMapper.writeValueAsString(TicketFactory.getDefaultTicketRequest());
        when(ticketService.addTicket(any())).thenReturn(TicketFactory.getDefaultTicket());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.TICKETS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.TICKETS_PATH + "/" + ID));
    }

    /**
     * Verifies that get Tickets By Projection Id.
     */
    @Test
    void testGetTicketsByProjectionId() throws Exception {
        final List<TicketDto> defaultTicketDtoList = TicketFactory.getDefaultTicketDtoList();
        final TicketDto defaultTicketDto = defaultTicketDtoList.getFirst();
        when(ticketService.getTicketsByProjectionId(anyInt())).thenReturn(defaultTicketDtoList);

        mockMvc
            .perform(get(URIConstants.PROJECTIONS_ID_TICKETS_PATH, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].dateOfPurchase").value(DATE_OF_PURCHASE.toString()))
            .andExpect(jsonPath("$[0].projection.id").value(defaultTicketDto.getProjection().getId()))
            .andExpect(jsonPath("$[0].projection.hall").value(defaultTicketDto.getProjection().getHall()))
            .andExpect(jsonPath("$[0].projection.price").value(defaultTicketDto.getProjection().getPrice()))
            .andExpect(jsonPath("$[0].projection.program.programDate").value(
                defaultTicketDto.getProjection().getProgram().getProgramDate().toString()))
            .andExpect(jsonPath("$[0].projection.program.cinema").value(
                defaultTicketDto.getProjection().getProgram().getCinema()))
            .andExpect(jsonPath("$[0].projection.movie.id").value(defaultTicketDto.getProjection().getMovie().getId()))
            .andExpect(
                jsonPath("$[0].projection.movie.title").value(defaultTicketDto.getProjection().getMovie().getTitle()))
            .andExpect(jsonPath("$[0].projection.movie.averageRating").value(
                defaultTicketDto.getProjection().getMovie().getAverageRating()))
            .andExpect(jsonPath("$[0].projection.movie.description").value(
                defaultTicketDto.getProjection().getMovie().getDescription()))
            .andExpect(jsonPath("$[0].projection.movie.releaseDate").value(
                defaultTicketDto.getProjection().getMovie().getReleaseDate().toString()))
            .andExpect(jsonPath("$[0].projection.movie.runtime").value(
                defaultTicketDto.getProjection().getMovie().getRuntime().toString()))
            .andExpect(jsonPath("$[0].projection.movie.category").value(
                defaultTicketDto.getProjection().getMovie().getCategory()))
            .andExpect(jsonPath("$[0].projection.startTime").value(
                defaultTicketDto.getProjection().getStartTime().toString()));
    }
}




