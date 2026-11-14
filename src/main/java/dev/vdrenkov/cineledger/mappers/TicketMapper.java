package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.entities.Ticket;

import java.util.List;

public final class TicketMapper {

    private TicketMapper() {}

    public static TicketDto mapTicketToTicketDto(Ticket ticket) {

        return new TicketDto(
                ticket.getId(),
                ticket.getDateOfPurchase(),
                ProjectionMapper.mapProjectionToProjectionDto(ticket.getProjection()),
                ticket.getUnitPrice(),
                ticket.getPaidPrice());
    }

    public static List<TicketDto> mapTicketToDtoList(List<Ticket> tickets) {
        return tickets.stream().map(TicketMapper::mapTicketToTicketDto).toList();
    }
}
