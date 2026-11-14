package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.models.entities.Program;

import java.util.List;

public final class ProgramMapper {

    private ProgramMapper() {}

    public static ProgramDto mapProgramToProgramDto(Program program) {

        return new ProgramDto(
                program.getId(),
                program.getProgramDate(),
                CinemaMapper.mapCinemaToCinemaDto(program.getCinema()));
    }

    public static List<ProgramDto> mapProgramListToProgramDtoList(List<Program> programs) {
        return programs.stream().map(ProgramMapper::mapProgramToProgramDto).toList();
    }
}
