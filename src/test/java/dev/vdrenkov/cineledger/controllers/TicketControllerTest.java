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

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TicketControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

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
            .andExpect(jsonPath("$[0].projection.id").value(defaultTicketDto.projection().id()))
            .andExpect(jsonPath("$[0].projection.hall").value(objectMapper.convertValue(defaultTicketDto.projection().hall(), java.util.Map.class)))
            .andExpect(jsonPath("$[0].projection.price").value(defaultTicketDto.projection().price()))
            .andExpect(jsonPath("$[0].projection.program.programDate").value(
                defaultTicketDto.projection().program().programDate().toString()))
            .andExpect(jsonPath("$[0].projection.program.cinema").value(
                objectMapper.convertValue(defaultTicketDto.projection().program().cinema(), java.util.Map.class)))
            .andExpect(jsonPath("$[0].projection.movie.id").value(defaultTicketDto.projection().movie().id()))
            .andExpect(
                jsonPath("$[0].projection.movie.title").value(defaultTicketDto.projection().movie().title()))
            .andExpect(jsonPath("$[0].projection.movie.averageRating").value(
                defaultTicketDto.projection().movie().averageRating()))
            .andExpect(jsonPath("$[0].projection.movie.description").value(
                defaultTicketDto.projection().movie().description()))
            .andExpect(jsonPath("$[0].projection.movie.releaseDate").value(
                defaultTicketDto.projection().movie().releaseDate().toString()))
            .andExpect(jsonPath("$[0].projection.movie.runtime").value(
                defaultTicketDto.projection().movie().runtime().toString()))
            .andExpect(jsonPath("$[0].projection.movie.category").value(
                objectMapper.convertValue(defaultTicketDto.projection().movie().category(), java.util.Map.class)))
            .andExpect(jsonPath("$[0].projection.startTime").value(
                defaultTicketDto.projection().startTime().toString()));
    }
}
