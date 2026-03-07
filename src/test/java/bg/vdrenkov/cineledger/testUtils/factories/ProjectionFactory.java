package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.entities.Projection;
import bg.vdrenkov.cineledger.models.requests.ProjectionRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import bg.vdrenkov.cineledger.models.dtos.ProjectionDto;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.ProjectionConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.ProjectionConstants.PRICE;
import static bg.vdrenkov.cineledger.testUtils.constants.ProjectionConstants.START_TIME;

public final class ProjectionFactory {

  private ProjectionFactory() throws IllegalAccessException {
    throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static ProjectionRequest getDefaultProjectionRequest() {
    return new ProjectionRequest(PRICE, HallFactory.getDefaultHall().getId(),
                                 ProgramFactory.getDefaultProgram().getId(),
                                 MovieFactory.getDefaultMovie().getId(), START_TIME);
  }

  public static Projection getDefaultProjection() {
    return new Projection(ID, PRICE, HallFactory.getDefaultHall(), ProgramFactory.getDefaultProgram(),
                          MovieFactory.getDefaultMovie(), START_TIME);
  }

  public static List<Projection> getDefaultProjectionList() {
    return Collections.singletonList(getDefaultProjection());
  }

  public static ProjectionDto getDefaultProjectionDto() {
    return new ProjectionDto(ID, PRICE, HallFactory.getDefaultHallDto(), ProgramFactory.getDefaultProgramDto(),
                             MovieFactory.getDefaultMovieDto(), START_TIME);
  }

  public static List<ProjectionDto> getDefaultProjectionDtoList() {
    return Collections.singletonList(getDefaultProjectionDto());
  }
}

