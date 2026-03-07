package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps ticket domain models to DTO representations used by the API.
 */
@Component
public class TicketMapper {
    private static final Logger log = LoggerFactory.getLogger(TicketMapper.class);

    private final ProjectionMapper projectionMapper;

    /**
     * Creates a new ticket mapper with its required collaborators.
     *
     * @param projectionMapper
     *     projection mapper used by the operation
     */
    @Autowired
    public TicketMapper(ProjectionMapper projectionMapper) {
        this.projectionMapper = projectionMapper;
    }

    /**
     * Maps ticket values to ticket dto values.
     *
     * @param ticket
     *     ticket entity to transform
     * @return ticket dto result
     */
    public TicketDto mapTicketToTicketDto(Ticket ticket) {
        log.info(String.format("The ticket with an id %d is being mapped to a ticket DTO", ticket.getId()));
        return new TicketDto(ticket.getId(), ticket.getDateOfPurchase(),
            projectionMapper.mapProjectionToProjectionDto(ticket.getProjection()));
    }

    /**
     * Maps ticket values to dto list values.
     *
     * @param tickets
     *     ticket entities or values to process
     * @return matching ticket dto values
     */
    public List<TicketDto> mapTicketToDtoList(List<Ticket> tickets) {
        return tickets.stream().map(this::mapTicketToTicketDto).collect(Collectors.toList());
    }
}


