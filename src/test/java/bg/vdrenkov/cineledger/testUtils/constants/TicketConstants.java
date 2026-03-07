package bg.vdrenkov.cineledger.testUtils.constants;

import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.LocalDate;

public final class TicketConstants {

    public static final int ID = 1;
    public static final LocalDate DATE_OF_PURCHASE = LocalDate.of(1999, 1, 1);

    private TicketConstants() throws IllegalAccessError {
        throw new IllegalAccessError(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


