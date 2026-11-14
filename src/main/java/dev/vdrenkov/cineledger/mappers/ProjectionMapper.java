package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.models.entities.Projection;

import java.util.List;

public final class ProjectionMapper {

    private ProjectionMapper() {

    }

    public static ProjectionDto mapProjectionToProjectionDto(Projection projection) {

        return new ProjectionDto(projection.getId(), projection.getPrice(),
            HallMapper.mapHallToHallDto(projection.getHall()),
            ProgramMapper.mapProgramToProgramDto(projection.getProgram()),
            MovieMapper.mapMovieToMovieDto(projection.getMovie()), projection.getStartTime());
    }

    public static List<ProjectionDto> mapProjectionListToProjectionDtoList(List<Projection> projections) {
        return projections.stream().map(ProjectionMapper::mapProjectionToProjectionDto).toList();
    }
}
