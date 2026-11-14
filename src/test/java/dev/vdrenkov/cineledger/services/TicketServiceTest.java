package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import dev.vdrenkov.cineledger.exceptions.DateNotValidException;
import dev.vdrenkov.cineledger.exceptions.NoAvailableTicketsException;
import dev.vdrenkov.cineledger.exceptions.TicketNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.entities.Projection;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import dev.vdrenkov.cineledger.models.requests.TicketRequest;
import dev.vdrenkov.cineledger.repositories.TicketRepository;
import dev.vdrenkov.cineledger.testutils.constants.ReportConstants;
import dev.vdrenkov.cineledger.testutils.constants.TicketConstants;
import dev.vdrenkov.cineledger.testutils.factories.ProjectionFactory;
import dev.vdrenkov.cineledger.testutils.factories.TicketFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock private TicketRepository ticketRepository;

    @Mock private ProjectionService projectionService;

    @InjectMocks private TicketService ticketService;

    @Test
    void testAddTicket_noExceptions_success() {
        final LocalDate programDate = LocalDate.now().plusDays(1);
        final LocalTime projectionStartTime = LocalTime.now().plusHours(1);

        final Projection projection = ProjectionFactory.getDefaultProjection();
        projection.getProgram().setProgramDate(programDate);
        projection.setStartTime(projectionStartTime);

        final TicketRequest request = new TicketRequest(projection.getId());

        final int availableTickets = 10;
        final Ticket expected = TicketFactory.getDefaultTicket();

        when(projectionService.getProjectionForUpdate(anyInt())).thenReturn(projection);
        when(ticketRepository.save(any())).thenReturn(expected);
        when(ticketRepository.countByProjectionId(anyInt()))
                .thenReturn(projection.getHall().getCapacity() - availableTickets);

        final Ticket ticket = ticketService.addTicket(request);

        assertEquals(expected, ticket);
    }

    @Test
    void testGetTicketsByProjectionId_noExceptions_success() {
        final List<TicketDto> expected = TicketFactory.getDefaultTicketDtoList();

        when(ticketRepository.findTicketByProjectionId(anyInt()))
                .thenReturn(TicketFactory.getDefaultTicketList());
        when(projectionService.getProjectionById(anyInt()))
                .thenReturn(ProjectionFactory.getDefaultProjection());

        final List<TicketDto> result = ticketService.getTicketsByProjectionId(TicketConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    void testGetTicketById_noExceptions_success() {
        final Ticket expected = TicketFactory.getDefaultTicket();

        when(ticketRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Ticket result = ticketService.getTicketById(TicketConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    void testGetTicketById_TicketNotFoundException_fail() {
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.getTicketById(TicketConstants.ID));
    }

    @Test
    void testCalculateAvailableTickets_shouldReturnCorrectAvailableTickets_success() {
        final Projection projection = ProjectionFactory.getDefaultProjection();
        final int capacity = 100;
        final int totalTickets = 50;

        when(ticketRepository.countByProjectionId(anyInt())).thenReturn(totalTickets);

        final int expectedAvailableTickets = capacity - totalTickets;
        final int actualAvailableTickets = ticketService.calculateAvailableTickets(projection);

        assertEquals(expectedAvailableTickets, actualAvailableTickets);
    }

    @Test
    void testCalculateAvailableTickets_shouldThrowNoAvailableTicketsException() {
        final Projection projection = ProjectionFactory.getDefaultProjection();
        final int totalTickets = 100;

        when(ticketRepository.countByProjectionId(anyInt())).thenReturn(totalTickets);

        assertThrows(
                NoAvailableTicketsException.class,
                () -> ticketService.calculateAvailableTickets(projection));
    }

    @Test
    void testAddTicket_dateNotValid_throwsDateNotValidException() {
        final LocalDate programDate = LocalDate.now().minusDays(1);
        final LocalTime projectionStartTime = LocalTime.now().minusHours(1);

        final Projection projection = ProjectionFactory.getDefaultProjection();
        projection.getProgram().setProgramDate(programDate);
        projection.setStartTime(projectionStartTime);

        final TicketRequest request = new TicketRequest(projection.getId());

        when(projectionService.getProjectionForUpdate(anyInt())).thenReturn(projection);

        assertThrows(DateNotValidException.class, () -> ticketService.addTicket(request));
    }

    @Test
    void testGetTicketsByDateBetween() {
        final List<Ticket> expected = TicketFactory.getDefaultTicketList();

        when(ticketRepository.findTicketsByDateOfPurchaseBetween(any(), any()))
                .thenReturn(expected);

        List<Ticket> tickets =
                ticketService.getTicketsByDateBetween(
                        ReportConstants.START_DATE, ReportConstants.END_DATE);

        assertEquals(expected, tickets);
    }
}
