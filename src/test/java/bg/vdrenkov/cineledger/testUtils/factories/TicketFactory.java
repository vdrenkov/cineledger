package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.dtos.TicketDto;
import bg.vdrenkov.cineledger.models.entities.Ticket;
import bg.vdrenkov.cineledger.models.requests.TicketRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.TicketConstants.DATE_OF_PURCHASE;
import static bg.vdrenkov.cineledger.testUtils.constants.TicketConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.factories.ProjectionFactory.getDefaultProjection;
import static bg.vdrenkov.cineledger.testUtils.factories.ProjectionFactory.getDefaultProjectionDto;

public final class TicketFactory {

    private TicketFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static TicketRequest getDefaultTicketRequest() {
        return new TicketRequest(ID);
    }

    public static Ticket getDefaultTicket() {
        return new Ticket(ID, DATE_OF_PURCHASE, getDefaultProjection());
    }

    public static List<Ticket> getDefaultTicketList() {
        return Collections.singletonList(getDefaultTicket());
    }

    public static TicketDto getDefaultTicketDto() {
        return new TicketDto(ID, DATE_OF_PURCHASE, getDefaultProjectionDto());
    }

    public static List<TicketDto> getDefaultTicketDtoList() {
        return Collections.singletonList(getDefaultTicketDto());
    }

    public static List<Integer> getDefaultIdList() {
        return Collections.singletonList(ID);
    }
}






