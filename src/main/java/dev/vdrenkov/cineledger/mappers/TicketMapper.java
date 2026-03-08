package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps ticket domain models to DTO representations used by the API.
 */
public final class TicketMapper {
    private static final Logger log = LoggerFactory.getLogger(TicketMapper.class);

    private TicketMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps ticket values to ticket dto values.
     *
     * @param ticket
     *     ticket entity to transform
     * @return ticket dto result
     */
    public static TicketDto mapTicketToTicketDto(Ticket ticket) {
        log.info("Mapping the ticket with an id {} to a ticket DTO", ticket.getId());
        return new TicketDto(ticket.getId(), ticket.getDateOfPurchase(),
            ProjectionMapper.mapProjectionToProjectionDto(ticket.getProjection()));
    }

    /**
     * Maps ticket values to dto list values.
     *
     * @param tickets
     *     ticket entities or values to process
     * @return matching ticket dto values
     */
    public static List<TicketDto> mapTicketToDtoList(List<Ticket> tickets) {
        return tickets.stream().map(TicketMapper::mapTicketToTicketDto).toList();
    }
}


