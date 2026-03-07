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

@Service
public class ProjectionService {

    private static final Logger log = LoggerFactory.getLogger(ProjectionService.class);

    private final ProjectionRepository projectionRepository;
    private final ProjectionMapper projectionMapper;
    private final ProgramService programService;
    private final HallService hallService;
    private final MovieService movieService;

    @Autowired
    public ProjectionService(ProjectionRepository projectionRepository, ProjectionMapper projectionMapper,
        ProgramService programService, HallService hallService, MovieService movieService) {
        this.projectionRepository = projectionRepository;
        this.projectionMapper = projectionMapper;
        this.programService = programService;
        this.hallService = hallService;
        this.movieService = movieService;
    }

    public Projection addProjection(ProjectionRequest request) {
        Hall hall = hallService.getHallById(request.getHallId());
        Program program = programService.getProgramById(request.getProgramId());
        Movie movie = movieService.getMovieById(request.getMovieId());
        LocalTime startTime = request.getStartTime();

        boolean isHallAvailable = isHallAvailable(hall.getId(), program.getId(), startTime);
        if (!isHallAvailable) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION));

            throw new HallNotAvailableException(ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION);
        }

        Projection projection = new Projection(request.getPrice(), hall, program, movie, request.getStartTime());

        log.info("An attempt to add a new projection in the database");

        return projectionRepository.save(projection);
    }

    public List<ProjectionDto> getProjectionsByProgramId(int programId) {
        Program program = programService.getProgramById(programId);

        log.info(String.format("All projections with program id %d were requested from the database", programId));

        return projectionMapper.mapProjectionListToProjectionDtoList(
            projectionRepository.findProjectionsByProgramId(program.getId()));
    }

    public List<ProjectionDto> getProjectionsByMovieId(int movieId) {
        Movie movie = movieService.getMovieById(movieId);

        log.info(String.format("All projections with movie id %d were requested from the database", movieId));

        return projectionMapper.mapProjectionListToProjectionDtoList(
            projectionRepository.findProjectionsByMovieId(movie.getId()));
    }

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

    public Projection getProjectionById(int id) {
        log.info(String.format("An attempt to extract a projection with an id %d from the database", id));

        return projectionRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("Exception caught: %s", ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE));

            throw new ProgramNotFoundException(ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE);
        });
    }

    public ProjectionDto getProjectionDtoById(int id) {
        log.info(String.format("An attempt to extract a projection DTO with an id %d from the database", id));

        return projectionMapper.mapProjectionToProjectionDto(getProjectionById(id));
    }

    public ProjectionDto updateProjection(ProjectionRequest request, int id) {
        ProjectionDto projectionDto = getProjectionDtoById(id);
        Hall hall = hallService.getHallById(request.getHallId());
        Program program = programService.getProgramById(request.getProgramId());
        Movie movie = movieService.getMovieById(request.getMovieId());
        LocalTime startTime = request.getStartTime();

        boolean isHallAvailable = isHallAvailable(hall.getId(), program.getId(), startTime);
        if (!isHallAvailable) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION));

            throw new HallNotAvailableException(ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION);
        }

        Projection projection = new Projection(id, request.getPrice(), hall, program, movie, startTime);
        projectionRepository.save(projection);

        log.info(String.format("Projection with an id %d has been updated", id));

        return projectionDto;
    }

    public ProjectionDto deleteProjection(int id) {
        ProjectionDto projectionDto = getProjectionDtoById(id);

        projectionRepository.deleteById(id);

        log.info(String.format("Projection with an id %d has been deleted", id));

        return projectionDto;
    }

    public boolean isHallAvailable(int hallId, int programId, LocalTime startTime) {
        LocalTime before = startTime.minusHours(3).minusMinutes(59);
        LocalTime after = startTime.plusHours(3).plusMinutes(59);

        if (before.isAfter(after)) {
            return isHallAvailableInPeriod(hallId, programId, before, LocalTime.MAX) && isHallAvailableInPeriod(hallId,
                programId, LocalTime.MIN, after);
        } else {
            return isHallAvailableInPeriod(hallId, programId, before, after);
        }
    }

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


