package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.entities.Cinema;
import bg.vdrenkov.cineledger.models.requests.CinemaRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import bg.vdrenkov.cineledger.models.dtos.CinemaDto;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.CinemaConstants.ADDRESS;
import static bg.vdrenkov.cineledger.testUtils.constants.CinemaConstants.CITY;
import static bg.vdrenkov.cineledger.testUtils.constants.CinemaConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.MovieConstants.RATING;

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

