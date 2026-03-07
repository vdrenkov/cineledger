package dev.vdrenkov.cineledger.testUtils.factories;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.requests.CinemaRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testUtils.constants.CinemaConstants.ADDRESS;
import static dev.vdrenkov.cineledger.testUtils.constants.CinemaConstants.CITY;
import static dev.vdrenkov.cineledger.testUtils.constants.CinemaConstants.ID;
import static dev.vdrenkov.cineledger.testUtils.constants.MovieConstants.RATING;

public final class CinemaFactory {

    private CinemaFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static CinemaRequest getDefaultCinemaRequest() {
        return new CinemaRequest(ADDRESS, CITY);
    }

    public static Cinema getDefaultCinema() {
        return new Cinema(ID, ADDRESS, CITY, RATING);
    }

    public static CinemaDto getDefaultCinemaDto() {
        return new CinemaDto(ID, ADDRESS, CITY, RATING);
    }

    public static List<CinemaDto> getDefaultCinemaDtoList() {
        return Collections.singletonList(getDefaultCinemaDto());
    }

    public static List<Cinema> getDefaultCinemaList() {
        return Collections.singletonList(getDefaultCinema());
    }
}

