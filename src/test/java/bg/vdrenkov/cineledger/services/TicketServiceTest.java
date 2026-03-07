package bg.vdrenkov.cineledger.services;

import org.junit.jupiter.api.extension.ExtendWith;
import bg.vdrenkov.cineledger.exceptions.DateNotValidException;
import bg.vdrenkov.cineledger.exceptions.NoAvailableTicketsException;
import bg.vdrenkov.cineledger.exceptions.TicketNotFoundException;
import bg.vdrenkov.cineledger.mappers.TicketMapper;
import bg.vdrenkov.cineledger.models.entities.Projection;
import bg.vdrenkov.cineledger.models.entities.Ticket;
import bg.vdrenkov.cineledger.models.requests.TicketRequest;
import bg.vdrenkov.cineledger.repositories.TicketRepository;
import bg.vdrenkov.cineledger.testUtils.constants.ReportConstants;
import bg.vdrenkov.cineledger.testUtils.constants.TicketConstants;
import bg.vdrenkov.cineledger.testUtils.factories.ProjectionFactory;
import bg.vdrenkov.cineledger.testUtils.factories.TicketFactory;
import bg.vdrenkov.cineledger.models.dtos.TicketDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ProjectionService projectionService;

    @InjectMocks
    private TicketService ticketService;

    @Test
    public void testAddTicket_noExceptions_success() {
        LocalDate programDate = LocalDate.now().plusDays(1);
        LocalTime projectionStartTime = LocalTime.now().plusHours(1);

        Projection projection = ProjectionFactory.getDefaultProjection();
        projection.getProgram().setProgramDate(programDate);
        projection.setStartTime(projectionStartTime);

        TicketRequest request = TicketFactory.getDefaultTicketRequest();
        request.setProjectionId(projection.getId());

        int availableTickets = 10;
        Ticket expected = TicketFactory.getDefaultTicket();

        when(projectionService.getProjectionById(anyInt())).thenReturn(projection);
        when(ticketRepository.save(any())).thenReturn(expected);
        when(ticketService.calculateAvailableTickets(projection)).thenReturn(availableTickets);

        Ticket ticket = ticketService.addTicket(request);

        assertEquals(expected, ticket);
    }


    @Test
    public void testGetTicketsByProjectionId_noExceptions_success() {
        List<TicketDto> expected = TicketFactory.getDefaultTicketDtoList();

        when(ticketRepository.findTicketByProjectionId(anyInt())).thenReturn(TicketFactory.getDefaultTicketList());
        when(projectionService.getProjectionById(anyInt())).thenReturn(ProjectionFactory.getDefaultProjection());
        when(ticketMapper.mapTicketToTicketDto(any())).thenReturn(TicketFactory.getDefaultTicketDto());

        List<TicketDto> result = ticketService.getTicketsByProjectionId(TicketConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    public void testGetTicketById_noExceptions_success() {
        Ticket expected = TicketFactory.getDefaultTicket();

        when(ticketRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Ticket result = ticketService.getTicketById(TicketConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    public void testGetTicketById_TicketNotFoundException_fail() {
      assertThrows(TicketNotFoundException.class, () -> {

          when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

          ticketService.getTicketById(TicketConstants.ID);
      
      });
    }

    @Test
    public void testCalculateAvailableTickets_shouldReturnCorrectAvailableTickets_success() {
        Projection projection = ProjectionFactory.getDefaultProjection();
        int capacity = 100;
        int totalTickets = 50;

        when(ticketRepository.countByProjectionId(anyInt())).thenReturn(totalTickets);

        int expectedAvailableTickets = capacity - totalTickets;
        int actualAvailableTickets = ticketService.calculateAvailableTickets(projection);

        assertEquals(expectedAvailableTickets, actualAvailableTickets);
    }

    @Test
    public void testCalculateAvailableTickets_shouldThrowNoAvailableTicketsException() {
      assertThrows(NoAvailableTicketsException.class, () -> {

          Projection projection = ProjectionFactory.getDefaultProjection();
          int totalTickets = 100;

          when(ticketRepository.countByProjectionId(anyInt())).thenReturn(totalTickets);

          ticketService.calculateAvailableTickets(projection);
      
      });
    }
    @Test
    public void testAddTicket_dateNotValid_throwsDateNotValidException() {
      assertThrows(DateNotValidException.class, () -> {

          LocalDate programDate = LocalDate.now().minusDays(1);
          LocalTime projectionStartTime = LocalTime.now().minusHours(1);

          Projection projection = ProjectionFactory.getDefaultProjection();
          projection.getProgram().setProgramDate(programDate);
          projection.setStartTime(projectionStartTime);

          TicketRequest request = TicketFactory.getDefaultTicketRequest();
          request.setProjectionId(projection.getId());

          when(projectionService.getProjectionById(anyInt())).thenReturn(projection);

          ticketService.addTicket(request);
      
      });
    }



    @Test
    public void testGetTicketsByDateBetween() {
        List<Ticket> expected = TicketFactory.getDefaultTicketList();

        when(ticketRepository.findTicketsByDateOfPurchaseBetween(any(), any())).thenReturn(expected);

        List<Ticket> tickets = ticketService.getTicketsByDateBetween(ReportConstants.START_DATE, ReportConstants.END_DATE);

        assertEquals(expected, tickets);
    }
}




