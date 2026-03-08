package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DateNotValidException;
import dev.vdrenkov.cineledger.exceptions.NoAvailableTicketsException;
import dev.vdrenkov.cineledger.exceptions.TicketNotFoundException;
import dev.vdrenkov.cineledger.mappers.TicketMapper;
import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.entities.Projection;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import dev.vdrenkov.cineledger.models.requests.TicketRequest;
import dev.vdrenkov.cineledger.repositories.TicketRepository;
import dev.vdrenkov.cineledger.testutil.constants.ReportConstants;
import dev.vdrenkov.cineledger.testutil.constants.TicketConstants;
import dev.vdrenkov.cineledger.testutil.factories.ProjectionFactory;
import dev.vdrenkov.cineledger.testutil.factories.TicketFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Tests ticket service behavior.
 */
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ProjectionService projectionService;

    @InjectMocks
    private TicketService ticketService;

    /**
     * Verifies that add Ticket no Exceptions success.
     */
    @Test
    void testAddTicket_noExceptions_success() {
        final LocalDate programDate = LocalDate.now().plusDays(1);
        final LocalTime projectionStartTime = LocalTime.now().plusHours(1);

        final Projection projection = ProjectionFactory.getDefaultProjection();
        projection.getProgram().setProgramDate(programDate);
        projection.setStartTime(projectionStartTime);

        final TicketRequest request = TicketFactory.getDefaultTicketRequest();
        request.setProjectionId(projection.getId());

        final int availableTickets = 10;
        final Ticket expected = TicketFactory.getDefaultTicket();

        when(projectionService.getProjectionById(anyInt())).thenReturn(projection);
        when(ticketRepository.save(any())).thenReturn(expected);
        when(ticketService.calculateAvailableTickets(projection)).thenReturn(availableTickets);

        final Ticket ticket = ticketService.addTicket(request);

        assertEquals(expected, ticket);
    }

    /**
     * Verifies that get Tickets By Projection Id no Exceptions success.
     */
    @Test
    void testGetTicketsByProjectionId_noExceptions_success() {
        final List<TicketDto> expected = TicketFactory.getDefaultTicketDtoList();

        when(ticketRepository.findTicketByProjectionId(anyInt())).thenReturn(TicketFactory.getDefaultTicketList());
        when(projectionService.getProjectionById(anyInt())).thenReturn(ProjectionFactory.getDefaultProjection());
        when(ticketMapper.mapTicketToTicketDto(any())).thenReturn(TicketFactory.getDefaultTicketDto());

        final List<TicketDto> result = ticketService.getTicketsByProjectionId(TicketConstants.ID);

        assertEquals(expected, result);
    }

    /**
     * Verifies that get Ticket By Id no Exceptions success.
     */
    @Test
    void testGetTicketById_noExceptions_success() {
        final Ticket expected = TicketFactory.getDefaultTicket();

        when(ticketRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Ticket result = ticketService.getTicketById(TicketConstants.ID);

        assertEquals(expected, result);
    }

    /**
     * Verifies that get Ticket By Id Ticket Not Found Exception fail.
     */
    @Test
    void testGetTicketById_TicketNotFoundException_fail() {
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(TicketConstants.ID));
    }

    /**
     * Verifies that calculate Available Tickets should Return Correct Available Tickets success.
     */
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

    /**
     * Verifies that calculate Available Tickets should Throw No Available Tickets Exception.
     */
    @Test
    void testCalculateAvailableTickets_shouldThrowNoAvailableTicketsException() {
        final Projection projection = ProjectionFactory.getDefaultProjection();
        final int totalTickets = 100;

        when(ticketRepository.countByProjectionId(anyInt())).thenReturn(totalTickets);

        assertThrows(NoAvailableTicketsException.class, () -> ticketService.calculateAvailableTickets(projection));
    }

    /**
     * Verifies that add Ticket date Not Valid throws Date Not Valid Exception.
     */
    @Test
    void testAddTicket_dateNotValid_throwsDateNotValidException() {
        final LocalDate programDate = LocalDate.now().minusDays(1);
        final LocalTime projectionStartTime = LocalTime.now().minusHours(1);

        final Projection projection = ProjectionFactory.getDefaultProjection();
        projection.getProgram().setProgramDate(programDate);
        projection.setStartTime(projectionStartTime);

        final TicketRequest request = TicketFactory.getDefaultTicketRequest();
        request.setProjectionId(projection.getId());

        when(projectionService.getProjectionById(anyInt())).thenReturn(projection);

        assertThrows(DateNotValidException.class, () -> ticketService.addTicket(request));
    }

    /**
     * Verifies that get Tickets By Date Between.
     */
    @Test
    void testGetTicketsByDateBetween() {
        final List<Ticket> expected = TicketFactory.getDefaultTicketList();

        when(ticketRepository.findTicketsByDateOfPurchaseBetween(any(), any())).thenReturn(expected);

        List<Ticket> tickets = ticketService.getTicketsByDateBetween(ReportConstants.START_DATE,
            ReportConstants.END_DATE);

        assertEquals(expected, tickets);
    }
}




