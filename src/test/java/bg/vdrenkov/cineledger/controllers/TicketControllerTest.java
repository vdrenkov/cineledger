package bg.vdrenkov.cineledger.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import bg.vdrenkov.cineledger.services.TicketService;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
import tools.jackson.databind.ObjectMapper;
import bg.vdrenkov.cineledger.models.dtos.TicketDto;
import bg.vdrenkov.cineledger.testUtils.factories.TicketFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.ProjectionConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.TicketConstants.DATE_OF_PURCHASE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

  private final static ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private TicketService ticketService;

  @InjectMocks
  private TicketController ticketController;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(ticketController)
      .build();
  }

  @Test
  public void testAddTicket_ticketAdded_success() throws Exception {
    String json = objectMapper.writeValueAsString(TicketFactory.getDefaultTicketRequest());
    when(ticketService.addTicket(any())).thenReturn(TicketFactory.getDefaultTicket());

    mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.TICKETS_PATH)
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", URIConstants.TICKETS_PATH + "/" + ID));
  }

  @Test
  public void testGetTicketsByProjectionId() throws Exception {
    List<TicketDto> defaultTicketDtoList = TicketFactory.getDefaultTicketDtoList();
    TicketDto defaultTicketDto = defaultTicketDtoList.get(0);
    when(ticketService.getTicketsByProjectionId(anyInt())).thenReturn(defaultTicketDtoList);

    mockMvc.perform(get(URIConstants.PROJECTIONS_ID_TICKETS_PATH, ID))
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
           .andExpect(
             jsonPath("$[0].projection.movie.id").value(defaultTicketDto.getProjection().getMovie().getId()))
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
           .andExpect(
             jsonPath("$[0].projection.startTime").value(defaultTicketDto.getProjection().getStartTime().toString()));
  }
}




