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
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final ProjectionService projectionService;
    private final TicketRepository ticketRepository;

    public TicketService(ProjectionService projectionService, TicketRepository ticketRepository) {
        this.projectionService = projectionService;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket addTicket(TicketRequest request) {
        final Projection projection =
                projectionService.getProjectionForUpdate(request.projectionId());

        final LocalDate programDate = projection.getProgram().getProgramDate();
        final LocalDate dateOfPurchase = LocalDate.now();

        if (!programDate.atTime(projection.getStartTime()).isAfter(LocalDateTime.now())) {
            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        calculateAvailableTickets(projection);

        final Ticket ticket = new Ticket(dateOfPurchase, projection);

        return ticketRepository.save(ticket);
    }

    public List<TicketDto> getTicketsByProjectionId(int id) {
        final Projection projection = this.projectionService.getProjectionById(id);

        final List<Ticket> tickets = ticketRepository.findTicketByProjectionId(projection.getId());

        return tickets.stream().map(TicketMapper::mapTicketToTicketDto).toList();
    }

    public Ticket getTicketForUpdate(final int id) {
        return ticketRepository
                .findByIdForUpdate(id)
                .orElseThrow(() ->
                        new TicketNotFoundException(ExceptionMessages.TICKET_NOT_FOUND_MESSAGE));
    }

    public void cancelTickets(final List<Ticket> tickets) {
        tickets.stream()
                .map(ticket -> ticket.getProjection().getId())
                .distinct()
                .sorted()
                .forEach(projectionService::getProjectionForUpdate);
        ticketRepository.deleteAll(tickets);
    }

    public int calculateAvailableTickets(Projection projection) {
        final int capacity = projection.getHall().getCapacity();
        final int totalTickets = ticketRepository.countByProjectionId(projection.getId());
        final int availableTickets = capacity - totalTickets;

        if (availableTickets <= 0) {
            throw new NoAvailableTicketsException(ExceptionMessages.NO_AVAILABLE_TICKETS_EXCEPTION);
        }

        return capacity - totalTickets;
    }
}
