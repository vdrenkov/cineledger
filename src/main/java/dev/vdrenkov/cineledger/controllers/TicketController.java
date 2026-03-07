package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import dev.vdrenkov.cineledger.models.requests.TicketRequest;
import dev.vdrenkov.cineledger.services.TicketService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Exposes REST endpoints for managing ticket data.
 */
@RestController
public class TicketController {

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);

    private final TicketService ticketService;

    /**
     * Creates a new ticket controller with its required collaborators.
     *
     * @param ticketService
     *     ticket service used by the operation
     */
    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Creates and persists ticket.
     *
     * @param request
     *     request payload containing the submitted data
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.TICKETS_PATH)
    public ResponseEntity<Void> addTicket(@RequestBody @Valid TicketRequest request) {
        final Ticket ticket = ticketService.addTicket(request);
        log.info("A request for a ticket to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.TICKETS_ID_PATH)
            .buildAndExpand(ticket.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Returns tickets matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.PROJECTIONS_ID_TICKETS_PATH)
    public ResponseEntity<List<TicketDto>> getTicketsByProjectionId(@PathVariable int id) {
        final List<TicketDto> ticketDtos = ticketService.getTicketsByProjectionId(id);
        log.info(String.format("All tickets with projection id %d were requested from the database", id));

        return ResponseEntity.ok(ticketDtos);
    }
}


