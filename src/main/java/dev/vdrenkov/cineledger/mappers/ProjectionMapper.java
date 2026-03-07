package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.models.entities.Projection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps projection domain models to DTO representations used by the API.
 */
@Component
public class ProjectionMapper {

    private static final Logger log = LoggerFactory.getLogger(ProjectionMapper.class);

    private final HallMapper hallMapper;
    private final ProgramMapper programMapper;
    private final MovieMapper movieMapper;

    /**
     * Creates a new projection mapper with its required collaborators.
     *
     * @param hallMapper
     *     hall mapper used by the operation
     * @param programMapper
     *     program mapper used by the operation
     * @param movieMapper
     *     movie mapper used by the operation
     */
    @Autowired
    public ProjectionMapper(HallMapper hallMapper, ProgramMapper programMapper, MovieMapper movieMapper) {
        this.hallMapper = hallMapper;
        this.programMapper = programMapper;
        this.movieMapper = movieMapper;
    }

    /**
     * Maps projection values to projection dto values.
     *
     * @param projection
     *     projection entity to transform
     * @return projection dto result
     */
    public ProjectionDto mapProjectionToProjectionDto(Projection projection) {
        log.info(String.format("The projection with an id %d is being mapped to a projection DTO", projection.getId()));
        return new ProjectionDto(projection.getId(), projection.getPrice(),
            hallMapper.mapHallToHallDto(projection.getHall()),
            programMapper.mapProgramToProgramDto(projection.getProgram()),
            movieMapper.mapMovieToMovieDto(projection.getMovie()), projection.getStartTime());
    }

    /**
     * Maps projection list values to projection dto list values.
     *
     * @param projections
     *     projection entities to transform
     * @return matching projection dto values
     */
    public List<ProjectionDto> mapProjectionListToProjectionDtoList(List<Projection> projections) {
        return projections.stream().map(this::mapProjectionToProjectionDto).collect(Collectors.toList());
    }
}


