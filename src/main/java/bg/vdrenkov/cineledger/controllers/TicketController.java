package bg.vdrenkov.cineledger.controllers;

import bg.vdrenkov.cineledger.models.dtos.TicketDto;
import bg.vdrenkov.cineledger.models.entities.Ticket;
import bg.vdrenkov.cineledger.models.requests.TicketRequest;
import bg.vdrenkov.cineledger.services.TicketService;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
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

@RestController
public class TicketController {

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(URIConstants.TICKETS_PATH)
    public ResponseEntity<Void> addTicket(@RequestBody @Valid TicketRequest request) {
        Ticket ticket = ticketService.addTicket(request);
        log.info("A request for a ticket to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.TICKETS_ID_PATH)
            .buildAndExpand(ticket.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.PROJECTIONS_ID_TICKETS_PATH)
    public ResponseEntity<List<TicketDto>> getTicketsByProjectionId(@PathVariable int id) {
        List<TicketDto> ticketDtos = ticketService.getTicketsByProjectionId(id);
        log.info(String.format("All tickets with projection id %d were requested from the database", id));

        return ResponseEntity.ok(ticketDtos);
    }
}


