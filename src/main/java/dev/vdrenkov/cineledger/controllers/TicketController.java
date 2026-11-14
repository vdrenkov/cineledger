package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import dev.vdrenkov.cineledger.models.requests.TicketRequest;
import dev.vdrenkov.cineledger.services.TicketService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;

import jakarta.validation.Valid;

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

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(URIConstants.TICKETS_PATH)
    public ResponseEntity<Void> addTicket(@RequestBody @Valid TicketRequest request) {
        final Ticket ticket = ticketService.addTicket(request);

        URI location =
                UriComponentsBuilder.fromUriString(URIConstants.TICKETS_ID_PATH)
                        .buildAndExpand(ticket.getId())
                        .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.PROJECTIONS_ID_TICKETS_PATH)
    public ResponseEntity<List<TicketDto>> getTicketsByProjectionId(@PathVariable int id) {
        final List<TicketDto> ticketDtos = ticketService.getTicketsByProjectionId(id);

        return ResponseEntity.ok(ticketDtos);
    }
}
