package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.models.requests.HallRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.HallConstants.CAPACITY;
import static dev.vdrenkov.cineledger.testutils.constants.HallConstants.ID;

/**
 * Provides reusable hall fixtures for tests.
 */
public final class HallFactory {

    private HallFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default hall request fixture used in tests.
     *
     * @return test hall request value
     */
    public static HallRequest getDefaultHallRequest() {
        return new HallRequest(CAPACITY, ID);
    }

    /**
     * Returns the default hall fixture used in tests.
     *
     * @return test hall value
     */
    public static Hall getDefaultHall() {
        return new Hall(ID, CAPACITY, CinemaFactory.getDefaultCinema());
    }

    /**
     * Returns the default hall list fixture used in tests.
     *
     * @return test hall values
     */
    public static List<Hall> getDefaultHallList() {
        return Collections.singletonList(getDefaultHall());
    }

    /**
     * Returns the default hall dto fixture used in tests.
     *
     * @return test hall dto value
     */
    public static HallDto getDefaultHallDto() {
        return new HallDto(ID, CAPACITY, CinemaFactory.getDefaultCinemaDto());
    }

    /**
     * Returns the default hall dto list fixture used in tests.
     *
     * @return test hall dto values
     */
    public static List<HallDto> getDefaultHallDtoList() {
        return Collections.singletonList(getDefaultHallDto());
    }
}

