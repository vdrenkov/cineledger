package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.testutil.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testutil.constants.TicketConstants;
import dev.vdrenkov.cineledger.testutil.factories.ProjectionFactory;
import dev.vdrenkov.cineledger.testutil.factories.TicketFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests ticket mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class TicketMapperTest {

    @Mock
    private ProjectionMapper projectionMapper;

    @InjectMocks
    private TicketMapper ticketMapper;

    /**
     * Verifies that map Ticket To Ticket DTO success.
     */
    @Test
    void testMapTicketToTicketDto_success() {
        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(
            ProjectionFactory.getDefaultProjectionDto());

        final TicketDto ticket = ticketMapper.mapTicketToTicketDto(TicketFactory.getDefaultTicket());

        assertEquals(TicketConstants.ID, ticket.getId());
        assertEquals(TicketConstants.DATE_OF_PURCHASE, ticket.getDateOfPurchase());
        assertEquals(ProjectionConstants.START_TIME, ticket.getProjection().getStartTime());
    }

    /**
     * Verifies that map Tickets To Tickets DTO success.
     */
    @Test
    void testMapTicketsToTicketsDto_success() {
        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(
            ProjectionFactory.getDefaultProjectionDto());

        final List<TicketDto> ticketDtos = ticketMapper.mapTicketToDtoList(TicketFactory.getDefaultTicketList());
        final TicketDto ticket = ticketDtos.getFirst();

        assertEquals(TicketConstants.ID, ticket.getId());
        assertEquals(TicketConstants.DATE_OF_PURCHASE, ticket.getDateOfPurchase());
        assertEquals(ProjectionConstants.START_TIME, ticket.getProjection().getStartTime());
    }
}




