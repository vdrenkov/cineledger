package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.models.entities.Projection;
import dev.vdrenkov.cineledger.models.requests.ProjectionRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants.PRICE;
import static dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants.START_TIME;

public final class ProjectionFactory {

    private ProjectionFactory() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static ProjectionRequest getDefaultProjectionRequest() {
        return new ProjectionRequest(PRICE, HallFactory.getDefaultHall().getId(),
            ProgramFactory.getDefaultProgram().getId(), MovieFactory.getDefaultMovie().getId(), START_TIME);
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
