package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.entities.Hall;
import bg.vdrenkov.cineledger.models.requests.HallRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import bg.vdrenkov.cineledger.models.dtos.HallDto;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.HallConstants.CAPACITY;
import static bg.vdrenkov.cineledger.testUtils.constants.HallConstants.ID;

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

