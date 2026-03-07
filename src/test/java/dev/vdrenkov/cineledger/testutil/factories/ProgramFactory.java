package dev.vdrenkov.cineledger.testutil.factories;

import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.models.entities.Program;
import dev.vdrenkov.cineledger.models.requests.ProgramRequest;
import dev.vdrenkov.cineledger.testutil.constants.CinemaConstants;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutil.constants.ProgramConstants.DATE;
import static dev.vdrenkov.cineledger.testutil.constants.ProgramConstants.ID;

/**
 * Provides reusable program fixtures for tests.
 */
public final class ProgramFactory {

    private ProgramFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default program fixture used in tests.
     *
     * @return test program value
     */
    public static Program getDefaultProgram() {
        return new Program(ID, DATE, CinemaFactory.getDefaultCinema());
    }

    /**
     * Returns the default program dto fixture used in tests.
     *
     * @return test program dto value
     */
    public static ProgramDto getDefaultProgramDto() {
        return new ProgramDto(ID, DATE, CinemaFactory.getDefaultCinemaDto());
    }

    /**
     * Returns the default program list fixture used in tests.
     *
     * @return test program values
     */
    public static List<Program> getDefaultProgramList() {
        return Collections.singletonList(getDefaultProgram());
    }

    /**
     * Returns the default program dto list fixture used in tests.
     *
     * @return test program dto values
     */
    public static List<ProgramDto> getDefaultProgramDtoList() {
        return Collections.singletonList(getDefaultProgramDto());
    }

    /**
     * Returns the default program request fixture used in tests.
     *
     * @return test program request value
     */
    public static ProgramRequest getDefaultProgramRequest() {
        return new ProgramRequest(DATE, CinemaConstants.ID);
    }
}


