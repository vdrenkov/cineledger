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
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ProjectionService {

    private final ProjectionRepository projectionRepository;
    private final ProgramService programService;
    private final HallService hallService;
    private final MovieService movieService;

    public ProjectionService(ProjectionRepository projectionRepository, ProgramService programService,
        HallService hallService, MovieService movieService) {
        this.projectionRepository = projectionRepository;
        this.programService = programService;
        this.hallService = hallService;
        this.movieService = movieService;
    }

    public Projection addProjection(ProjectionRequest request) {
        final Hall hall = hallService.getHallById(request.hallId());
        final Program program = programService.getProgramById(request.programId());
        final Movie movie = movieService.getMovieById(request.movieId());
        final LocalTime startTime = request.startTime();

        final boolean isHallAvailable = isHallAvailable(hall.getId(), program.getId(), startTime);
        if (!isHallAvailable) {

            throw new HallNotAvailableException(ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION);
        }

        final Projection projection = new Projection(request.price(), hall, program, movie, request.startTime());

        return projectionRepository.save(projection);
    }

    public List<ProjectionDto> getProjectionsByProgramId(int programId) {
        final Program program = programService.getProgramById(programId);

        return ProjectionMapper.mapProjectionListToProjectionDtoList(
            projectionRepository.findProjectionsByProgramId(program.getId()));
    }

    public List<ProjectionDto> getProjectionsByMovieId(int movieId) {
        final Movie movie = movieService.getMovieById(movieId);

        return ProjectionMapper.mapProjectionListToProjectionDtoList(
            projectionRepository.findProjectionsByMovieId(movie.getId()));
    }

    public List<ProjectionDto> getProjectionsByStartTime(LocalTime startTime, boolean isBefore) {
        List<Projection> projections;

        if (isBefore) {

            projections = projectionRepository.findProjectionsByStartTimeBefore(startTime);
        } else {

            projections = projectionRepository.findProjectionsByStartTimeAfter(startTime);
        }

        return ProjectionMapper.mapProjectionListToProjectionDtoList(projections);
    }

    public Projection getProjectionById(int id) {

        return projectionRepository.findById(id).orElseThrow(() -> {

            return new ProgramNotFoundException(ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE);
        });
    }

    public ProjectionDto getProjectionDtoById(int id) {

        return ProjectionMapper.mapProjectionToProjectionDto(getProjectionById(id));
    }

    public ProjectionDto updateProjection(ProjectionRequest request, int id) {
        final ProjectionDto projectionDto = getProjectionDtoById(id);
        final Hall hall = hallService.getHallById(request.hallId());
        final Program program = programService.getProgramById(request.programId());
        final Movie movie = movieService.getMovieById(request.movieId());
        final LocalTime startTime = request.startTime();

        final boolean isHallAvailable = isHallAvailable(hall.getId(), program.getId(), startTime);
        if (!isHallAvailable) {

            throw new HallNotAvailableException(ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION);
        }

        final Projection projection = new Projection(id, request.price(), hall, program, movie, startTime);
        projectionRepository.save(projection);

        return projectionDto;
    }

    public ProjectionDto deleteProjection(int id) {
        final ProjectionDto projectionDto = getProjectionDtoById(id);

        projectionRepository.deleteById(id);

        return projectionDto;
    }

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
