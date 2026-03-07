package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.testUtils.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testUtils.constants.TicketConstants;
import dev.vdrenkov.cineledger.testUtils.factories.ProjectionFactory;
import dev.vdrenkov.cineledger.testUtils.factories.TicketFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketMapperTest {

    @Mock
    private ProjectionMapper projectionMapper;

    @InjectMocks
    private TicketMapper ticketMapper;

    @Test
    public void testMapTicketToTicketDto_success() {
        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(
            ProjectionFactory.getDefaultProjectionDto());

        TicketDto ticket = ticketMapper.mapTicketToTicketDto(TicketFactory.getDefaultTicket());

        Assertions.assertEquals(ticket.getId(), TicketConstants.ID);
        Assertions.assertEquals(ticket.getDateOfPurchase(), TicketConstants.DATE_OF_PURCHASE);
        Assertions.assertEquals(ticket.getProjection().getStartTime(), ProjectionConstants.START_TIME);
    }

    @Test
    public void testMapTicketsToTicketsDto_success() {
        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(
            ProjectionFactory.getDefaultProjectionDto());

        List<TicketDto> ticketDtos = ticketMapper.mapTicketToDtoList(TicketFactory.getDefaultTicketList());
        TicketDto ticket = ticketDtos.get(0);

        Assertions.assertEquals(ticket.getId(), TicketConstants.ID);
        Assertions.assertEquals(ticket.getDateOfPurchase(), TicketConstants.DATE_OF_PURCHASE);
        assertEquals(ticket.getProjection().getStartTime(), ProjectionConstants.START_TIME);
    }
}




