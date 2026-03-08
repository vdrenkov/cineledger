package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testutils.constants.TicketConstants;
import dev.vdrenkov.cineledger.testutils.factories.TicketFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests ticket mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class TicketMapperTest {
    /**
     * Verifies that map Ticket To Ticket DTO success.
     */
    @Test
    void testMapTicketToTicketDto_success() {
        final TicketDto ticket = TicketMapper.mapTicketToTicketDto(TicketFactory.getDefaultTicket());

        assertEquals(TicketConstants.ID, ticket.getId());
        assertEquals(TicketConstants.DATE_OF_PURCHASE, ticket.getDateOfPurchase());
        assertEquals(ProjectionConstants.START_TIME, ticket.getProjection().getStartTime());
    }

    /**
     * Verifies that map Tickets To Tickets DTO success.
     */
    @Test
    void testMapTicketsToTicketsDto_success() {
        final List<TicketDto> ticketDtos = TicketMapper.mapTicketToDtoList(TicketFactory.getDefaultTicketList());
        final TicketDto ticket = ticketDtos.getFirst();

        assertEquals(TicketConstants.ID, ticket.getId());
        assertEquals(TicketConstants.DATE_OF_PURCHASE, ticket.getDateOfPurchase());
        assertEquals(ProjectionConstants.START_TIME, ticket.getProjection().getStartTime());
    }
}




