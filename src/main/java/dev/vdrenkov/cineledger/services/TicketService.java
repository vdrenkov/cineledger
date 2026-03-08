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
import dev.vdrenkov.cineledger.utils.constants.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Contains business logic for ticket operations.
 */
@Service
public class TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final ProjectionService projectionService;
    private final TicketRepository ticketRepository;

    /**
     * Creates a new ticket service with its required collaborators.
     *
     * @param projectionService
     *     projection service used by the operation
     * @param ticketRepository
     *     ticket repository used by the operation
     */
    @Autowired
    public TicketService(ProjectionService projectionService, TicketRepository ticketRepository) {
        this.projectionService = projectionService;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Creates and persists ticket.
     *
     * @param request
     *     request payload containing the submitted data
     * @return requested ticket value
     */
    public Ticket addTicket(TicketRequest request) {
        final Projection projection = projectionService.getProjectionById(request.getProjectionId());

        final LocalDate programDate = projection.getProgram().getProgramDate();
        final LocalDate dateOfPurchase = LocalDate.now();

        if (programDate.isBefore(dateOfPurchase)) {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.DATE_NOT_VALID_MESSAGE);
            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        calculateAvailableTickets(projection);

        final Ticket ticket = new Ticket(dateOfPurchase, projection);

        log.info("An attempt to add a new ticket in the database");

        return ticketRepository.save(ticket);
    }

    /**
     * Returns tickets matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return matching ticket dto values
     */
    public List<TicketDto> getTicketsByProjectionId(int id) {
        final Projection projection = this.projectionService.getProjectionById(id);

        final List<Ticket> tickets = ticketRepository.findTicketByProjectionId(projection.getId());

        log.info("All tickets with projection id {} requested from the database", id);

        return tickets.stream().map(TicketMapper::mapTicketToTicketDto).toList();
    }

    /**
     * Returns ticket matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested ticket value
     */
    public Ticket getTicketById(int id) {
        log.info("Retrieving ticket with id {} from database", id);

        return ticketRepository.findById(id).orElseThrow(() -> {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.TICKET_NOT_FOUND_MESSAGE);

            return new TicketNotFoundException(ExceptionMessages.TICKET_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns tickets matching the supplied criteria.
     *
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return matching ticket values
     */
    public List<Ticket> getTicketsByDateBetween(LocalDate startDate, LocalDate endDate) {
        log.info("All tickets with date between {} and {} requested from the database", startDate, endDate);

        return ticketRepository.findTicketsByDateOfPurchaseBetween(startDate, endDate);
    }

    /**
     * Executes the calculate available tickets operation for ticket.
     *
     * @param projection
     *     projection entity to transform
     * @return requested int value
     */
    public int calculateAvailableTickets(Projection projection) {
        final int capacity = projection.getHall().getCapacity();
        final int totalTickets = ticketRepository.countByProjectionId(projection.getId());
        final int availableTickets = capacity - totalTickets;

        if (availableTickets <= 0) {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.NO_AVAILABLE_TICKETS_EXCEPTION);

            throw new NoAvailableTicketsException(ExceptionMessages.NO_AVAILABLE_TICKETS_EXCEPTION);
        }

        return capacity - totalTickets;
    }
}


