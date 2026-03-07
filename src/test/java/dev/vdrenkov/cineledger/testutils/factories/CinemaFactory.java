package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.requests.CinemaRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.CinemaConstants.ADDRESS;
import static dev.vdrenkov.cineledger.testutils.constants.CinemaConstants.CITY;
import static dev.vdrenkov.cineledger.testutils.constants.CinemaConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.MovieConstants.RATING;

/**
 * Provides reusable cinema fixtures for tests.
 */
public final class CinemaFactory {

    private CinemaFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default cinema request fixture used in tests.
     *
     * @return test cinema request value
     */
    public static CinemaRequest getDefaultCinemaRequest() {
        return new CinemaRequest(ADDRESS, CITY);
    }

    /**
     * Returns the default cinema fixture used in tests.
     *
     * @return test cinema value
     */
    public static Cinema getDefaultCinema() {
        return new Cinema(ID, ADDRESS, CITY, RATING);
    }

    /**
     * Returns the default cinema dto fixture used in tests.
     *
     * @return test cinema dto value
     */
    public static CinemaDto getDefaultCinemaDto() {
        return new CinemaDto(ID, ADDRESS, CITY, RATING);
    }

    /**
     * Returns the default cinema dto list fixture used in tests.
     *
     * @return test cinema dto values
     */
    public static List<CinemaDto> getDefaultCinemaDtoList() {
        return Collections.singletonList(getDefaultCinemaDto());
    }

    /**
     * Returns the default cinema list fixture used in tests.
     *
     * @return test cinema values
     */
    public static List<Cinema> getDefaultCinemaList() {
        return Collections.singletonList(getDefaultCinema());
    }
}

