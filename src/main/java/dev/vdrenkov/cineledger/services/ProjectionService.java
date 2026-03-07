package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.HallNotAvailableException;
import dev.vdrenkov.cineledger.exceptions.ProgramNotFoundException;
import dev.vdrenkov.cineledger.mappers.ProjectionMapper;
import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.entities.Program;
import dev.vdrenkov.cineledger.models.entities.Projection;
import dev.vdrenkov.cineledger.models.requests.ProjectionRequest;
import dev.vdrenkov.cineledger.repositories.ProjectionRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

/**
 * Contains business logic for projection operations.
 */
@Service
public class ProjectionService {
    private static final Logger log = LoggerFactory.getLogger(ProjectionService.class);

    private final ProjectionRepository projectionRepository;
    private final ProjectionMapper projectionMapper;
    private final ProgramService programService;
    private final HallService hallService;
    private final MovieService movieService;

    /**
     * Creates a new projection service with its required collaborators.
     *
     * @param projectionRepository
     *     projection repository used by the operation
     * @param projectionMapper
     *     projection mapper used by the operation
     * @param programService
     *     program service used by the operation
     * @param hallService
     *     hall service used by the operation
     * @param movieService
     *     movie service used by the operation
     */
    @Autowired
    public ProjectionService(ProjectionRepository projectionRepository, ProjectionMapper projectionMapper,
        ProgramService programService, HallService hallService, MovieService movieService) {
        this.projectionRepository = projectionRepository;
        this.projectionMapper = projectionMapper;
        this.programService = programService;
        this.hallService = hallService;
        this.movieService = movieService;
    }

    /**
     * Creates and persists projection.
     *
     * @param request
     *     request payload containing the submitted data
     * @return requested projection value
     */
    public Projection addProjection(ProjectionRequest request) {
        final Hall hall = hallService.getHallById(request.getHallId());
        final Program program = programService.getProgramById(request.getProgramId());
        final Movie movie = movieService.getMovieById(request.getMovieId());
        final LocalTime startTime = request.getStartTime();

        final boolean isHallAvailable = isHallAvailable(hall.getId(), program.getId(), startTime);
        if (!isHallAvailable) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION));

            throw new HallNotAvailableException(ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION);
        }

        final Projection projection = new Projection(request.getPrice(), hall, program, movie, request.getStartTime());

        log.info("An attempt to add a new projection in the database");

        return projectionRepository.save(projection);
    }

    /**
     * Returns projections matching the supplied criteria.
     *
     * @param programId
     *     identifier of the target program
     * @return matching projection dto values
     */
    public List<ProjectionDto> getProjectionsByProgramId(int programId) {
        final Program program = programService.getProgramById(programId);

        log.info(String.format("All projections with program id %d were requested from the database", programId));

        return projectionMapper.mapProjectionListToProjectionDtoList(
            projectionRepository.findProjectionsByProgramId(program.getId()));
    }

    /**
     * Returns projections matching the supplied criteria.
     *
     * @param movieId
     *     identifier of the target movie
     * @return matching projection dto values
     */
    public List<ProjectionDto> getProjectionsByMovieId(int movieId) {
        final Movie movie = movieService.getMovieById(movieId);

        log.info(String.format("All projections with movie id %d were requested from the database", movieId));

        return projectionMapper.mapProjectionListToProjectionDtoList(
            projectionRepository.findProjectionsByMovieId(movie.getId()));
    }

    /**
     * Returns projections matching the supplied criteria.
     *
     * @param startTime
     *     start time used by the operation
     * @param isBefore
     *     whether to match data before the provided boundary
     * @return matching projection dto values
     */
    public List<ProjectionDto> getProjectionsByStartTime(LocalTime startTime, Boolean isBefore) {
        List<Projection> projections;

        if (isBefore) {
            log.info(
                String.format("All projections by start time before %s were requested from the database", startTime));

            projections = projectionRepository.findProjectionsByStartTimeBefore(startTime);
        } else {
            log.info(
                String.format("All projections by start time after %s were requested from the database", startTime));

            projections = projectionRepository.findProjectionsByStartTimeAfter(startTime);
        }

        return projectionMapper.mapProjectionListToProjectionDtoList(projections);
    }

    /**
     * Returns projection matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested projection value
     */
    public Projection getProjectionById(int id) {
        log.info(String.format("An attempt to extract a projection with an id %d from the database", id));

        return projectionRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("Exception caught: %s", ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE));

            throw new ProgramNotFoundException(ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns projection matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return projection dto result
     */
    public ProjectionDto getProjectionDtoById(int id) {
        log.info(String.format("An attempt to extract a projection DTO with an id %d from the database", id));

        return projectionMapper.mapProjectionToProjectionDto(getProjectionById(id));
    }

    /**
     * Updates projection and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @return projection dto result
     */
    public ProjectionDto updateProjection(ProjectionRequest request, int id) {
        final ProjectionDto projectionDto = getProjectionDtoById(id);
        final Hall hall = hallService.getHallById(request.getHallId());
        final Program program = programService.getProgramById(request.getProgramId());
        final Movie movie = movieService.getMovieById(request.getMovieId());
        final LocalTime startTime = request.getStartTime();

        final boolean isHallAvailable = isHallAvailable(hall.getId(), program.getId(), startTime);
        if (!isHallAvailable) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION));

            throw new HallNotAvailableException(ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION);
        }

        final Projection projection = new Projection(id, request.getPrice(), hall, program, movie, startTime);
        projectionRepository.save(projection);

        log.info(String.format("Projection with an id %d has been updated", id));

        return projectionDto;
    }

    /**
     * Deletes projection and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @return projection dto result
     */
    public ProjectionDto deleteProjection(int id) {
        final ProjectionDto projectionDto = getProjectionDtoById(id);

        projectionRepository.deleteById(id);

        log.info(String.format("Projection with an id %d has been deleted", id));

        return projectionDto;
    }

    /**
     * Checks whether hall available satisfies the required condition.
     *
     * @param hallId
     *     identifier of the target hall
     * @param programId
     *     identifier of the target program
     * @param startTime
     *     start time used by the operation
     * @return true when the requested condition holds; otherwise false
     */
    public boolean isHallAvailable(int hallId, int programId, LocalTime startTime) {
        final LocalTime before = startTime.minusHours(3).minusMinutes(59);
        final LocalTime after = startTime.plusHours(3).plusMinutes(59);

        if (before.isAfter(after)) {
            return isHallAvailableInPeriod(hallId, programId, before, LocalTime.MAX) && isHallAvailableInPeriod(hallId,
                programId, LocalTime.MIN, after);
        } else {
            return isHallAvailableInPeriod(hallId, programId, before, after);
        }
    }

    /**
     * Checks whether hall available in period satisfies the required condition.
     *
     * @param hallId
     *     identifier of the target hall
     * @param programId
     *     identifier of the target program
     * @param start
     *     start used by the operation
     * @param end
     *     end used by the operation
     * @return true when the requested condition holds; otherwise false
     */
    public boolean isHallAvailableInPeriod(int hallId, int programId, LocalTime start, LocalTime end) {
        List<Projection> projections = projectionRepository.findProjectionsByHallIdAndStartTimeBetween(hallId, start,
            end);

        for (Projection projection : projections) {
            if (projection.getProgram().getId() == programId) {
                return false;
            }
        }
        return true;
    }
}


