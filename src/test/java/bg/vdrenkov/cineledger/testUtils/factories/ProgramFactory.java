package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.entities.Program;
import bg.vdrenkov.cineledger.models.requests.ProgramRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import bg.vdrenkov.cineledger.models.dtos.ProgramDto;
import bg.vdrenkov.cineledger.testUtils.constants.CinemaConstants;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.ProgramConstants.DATE;
import static bg.vdrenkov.cineledger.testUtils.constants.ProgramConstants.ID;

public final class ProgramFactory {

  private ProgramFactory() throws IllegalAccessException {
    throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static Program getDefaultProgram() {
    return new Program(ID, DATE, CinemaFactory.getDefaultCinema());
  }

  public static ProgramDto getDefaultProgramDto() {
    return new ProgramDto(ID, DATE, CinemaFactory.getDefaultCinemaDto());
  }

  public static List<Program> getDefaultProgramList() {
    return Collections.singletonList(getDefaultProgram());
  }

  public static List<ProgramDto> getDefaultProgramDtoList() {
    return Collections.singletonList(getDefaultProgramDto());
  }

  public static ProgramRequest getDefaultProgramRequest() {
    return new ProgramRequest(DATE, CinemaConstants.ID);
  }
}


