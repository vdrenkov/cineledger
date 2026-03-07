package dev.vdrenkov.cineledger.testUtils.factories;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.models.requests.HallRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testUtils.constants.HallConstants.CAPACITY;
import static dev.vdrenkov.cineledger.testUtils.constants.HallConstants.ID;

public final class HallFactory {

    private HallFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static HallRequest getDefaultHallRequest() {
        return new HallRequest(CAPACITY, ID);
    }

    public static Hall getDefaultHall() {
        return new Hall(ID, CAPACITY, CinemaFactory.getDefaultCinema());
    }

    public static List<Hall> getDefaultHallList() {
        return Collections.singletonList(getDefaultHall());
    }

    public static HallDto getDefaultHallDto() {
        return new HallDto(ID, CAPACITY, CinemaFactory.getDefaultCinemaDto());
    }

    public static List<HallDto> getDefaultHallDtoList() {
        return Collections.singletonList(getDefaultHallDto());
    }
}

