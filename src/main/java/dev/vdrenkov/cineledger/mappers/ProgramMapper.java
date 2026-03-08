package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.models.entities.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps program domain models to DTO representations used by the API.
 */
public final class ProgramMapper {
    private static final Logger log = LoggerFactory.getLogger(ProgramMapper.class);

    private ProgramMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps program values to program dto values.
     *
     * @param program
     *     program entity to transform
     * @return program dto result
     */
    public static ProgramDto mapProgramToProgramDto(Program program) {
        log.info("Mapping the program with an id {} to a program DTO", program.getId());
        return new ProgramDto(program.getId(), program.getProgramDate(),
            CinemaMapper.mapCinemaToCinemaDto(program.getCinema()));
    }

    /**
     * Maps program list values to program dto list values.
     *
     * @param programs
     *     program entities to transform
     * @return matching program dto values
     */
    public static List<ProgramDto> mapProgramListToProgramDtoList(List<Program> programs) {
        return programs.stream().map(ProgramMapper::mapProgramToProgramDto).toList();
    }
}


