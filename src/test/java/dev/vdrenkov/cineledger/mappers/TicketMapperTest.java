package dev.vdrenkov.cineledger.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testutils.constants.TicketConstants;
import dev.vdrenkov.cineledger.testutils.factories.TicketFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TicketMapperTest {

    @Test
    void testMapTicketToTicketDto_success() {
        final TicketDto ticket =
                TicketMapper.mapTicketToTicketDto(TicketFactory.getDefaultTicket());

        assertEquals(TicketConstants.ID, ticket.id());
        assertEquals(TicketConstants.DATE_OF_PURCHASE, ticket.dateOfPurchase());
        assertEquals(ProjectionConstants.START_TIME, ticket.projection().startTime());
    }

    @Test
    void testMapTicketsToTicketsDto_success() {
        final List<TicketDto> ticketDtos =
                TicketMapper.mapTicketToDtoList(TicketFactory.getDefaultTicketList());
        final TicketDto ticket = ticketDtos.getFirst();

        assertEquals(TicketConstants.ID, ticket.id());
        assertEquals(TicketConstants.DATE_OF_PURCHASE, ticket.dateOfPurchase());
        assertEquals(ProjectionConstants.START_TIME, ticket.projection().startTime());
    }
}
