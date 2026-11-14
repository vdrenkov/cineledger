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

import java.time.LocalDate;
import java.util.List;

@Service
public class TicketService {

    private final ProjectionService projectionService;
    private final TicketRepository ticketRepository;

    public TicketService(ProjectionService projectionService, TicketRepository ticketRepository) {
        this.projectionService = projectionService;
        this.ticketRepository = ticketRepository;
    }

    public Ticket addTicket(TicketRequest request) {
        final Projection projection = projectionService.getProjectionById(request.projectionId());

        final LocalDate programDate = projection.getProgram().getProgramDate();
        final LocalDate dateOfPurchase = LocalDate.now();

        if (programDate.isBefore(dateOfPurchase)) {

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

    public Ticket getTicketById(int id) {

        return ticketRepository.findById(id).orElseThrow(() -> {

            return new TicketNotFoundException(ExceptionMessages.TICKET_NOT_FOUND_MESSAGE);
        });
    }

    public List<Ticket> getTicketsByDateBetween(LocalDate startDate, LocalDate endDate) {

        return ticketRepository.findTicketsByDateOfPurchaseBetween(startDate, endDate);
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
