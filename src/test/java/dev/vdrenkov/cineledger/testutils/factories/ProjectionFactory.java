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

/**
 * Provides reusable projection fixtures for tests.
 */
public final class ProjectionFactory {

    private ProjectionFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default projection request fixture used in tests.
     *
     * @return test projection request value
     */
    public static ProjectionRequest getDefaultProjectionRequest() {
        return new ProjectionRequest(PRICE, HallFactory.getDefaultHall().getId(),
            ProgramFactory.getDefaultProgram().getId(), MovieFactory.getDefaultMovie().getId(), START_TIME);
    }

    /**
     * Returns the default projection fixture used in tests.
     *
     * @return test projection value
     */
    public static Projection getDefaultProjection() {
        return new Projection(ID, PRICE, HallFactory.getDefaultHall(), ProgramFactory.getDefaultProgram(),
            MovieFactory.getDefaultMovie(), START_TIME);
    }

    /**
     * Returns the default projection list fixture used in tests.
     *
     * @return test projection values
     */
    public static List<Projection> getDefaultProjectionList() {
        return Collections.singletonList(getDefaultProjection());
    }

    /**
     * Returns the default projection dto fixture used in tests.
     *
     * @return test projection dto value
     */
    public static ProjectionDto getDefaultProjectionDto() {
        return new ProjectionDto(ID, PRICE, HallFactory.getDefaultHallDto(), ProgramFactory.getDefaultProgramDto(),
            MovieFactory.getDefaultMovieDto(), START_TIME);
    }

    /**
     * Returns the default projection dto list fixture used in tests.
     *
     * @return test projection dto values
     */
    public static List<ProjectionDto> getDefaultProjectionDtoList() {
        return Collections.singletonList(getDefaultProjectionDto());
    }
}

