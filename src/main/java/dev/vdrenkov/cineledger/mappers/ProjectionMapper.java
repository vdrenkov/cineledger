package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.models.entities.Projection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps projection domain models to DTO representations used by the API.
 */
public final class ProjectionMapper {
    private static final Logger log = LoggerFactory.getLogger(ProjectionMapper.class);

    private ProjectionMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps projection values to projection dto values.
     *
     * @param projection
     *     projection entity to transform
     * @return projection dto result
     */
    public static ProjectionDto mapProjectionToProjectionDto(Projection projection) {
        log.info("Mapping the projection with an id {} to a projection DTO", projection.getId());
        return new ProjectionDto(projection.getId(), projection.getPrice(),
            HallMapper.mapHallToHallDto(projection.getHall()),
            ProgramMapper.mapProgramToProgramDto(projection.getProgram()),
            MovieMapper.mapMovieToMovieDto(projection.getMovie()), projection.getStartTime());
    }

    /**
     * Maps projection list values to projection dto list values.
     *
     * @param projections
     *     projection entities to transform
     * @return matching projection dto values
     */
    public static List<ProjectionDto> mapProjectionListToProjectionDtoList(List<Projection> projections) {
        return projections.stream().map(ProjectionMapper::mapProjectionToProjectionDto).toList();
    }
}


