package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import dev.vdrenkov.cineledger.models.requests.TicketRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.TicketConstants.DATE_OF_PURCHASE;
import static dev.vdrenkov.cineledger.testutils.constants.TicketConstants.ID;
import static dev.vdrenkov.cineledger.testutils.factories.ProjectionFactory.getDefaultProjection;
import static dev.vdrenkov.cineledger.testutils.factories.ProjectionFactory.getDefaultProjectionDto;

/**
 * Provides reusable ticket fixtures for tests.
 */
public final class TicketFactory {

    private TicketFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default ticket request fixture used in tests.
     *
     * @return test ticket request value
     */
    public static TicketRequest getDefaultTicketRequest() {
        return new TicketRequest(ID);
    }

    /**
     * Returns the default ticket fixture used in tests.
     *
     * @return test ticket value
     */
    public static Ticket getDefaultTicket() {
        return new Ticket(ID, DATE_OF_PURCHASE, getDefaultProjection());
    }

    /**
     * Returns the default ticket list fixture used in tests.
     *
     * @return test ticket values
     */
    public static List<Ticket> getDefaultTicketList() {
        return Collections.singletonList(getDefaultTicket());
    }

    /**
     * Returns the default ticket dto fixture used in tests.
     *
     * @return test ticket dto value
     */
    public static TicketDto getDefaultTicketDto() {
        return new TicketDto(ID, DATE_OF_PURCHASE, getDefaultProjectionDto());
    }

    /**
     * Returns the default ticket dto list fixture used in tests.
     *
     * @return test ticket dto values
     */
    public static List<TicketDto> getDefaultTicketDtoList() {
        return Collections.singletonList(getDefaultTicketDto());
    }

    /**
     * Returns the default id list fixture used in tests.
     *
     * @return test integer values
     */
    public static List<Integer> getDefaultIdList() {
        return Collections.singletonList(ID);
    }
}






