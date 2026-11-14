package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.HallNotAvailableException;
import dev.vdrenkov.cineledger.exceptions.ProjectionNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
public class ProjectionService {
    private final dev.vdrenkov.cineledger.repositories.TicketRepository ticketRepository;

    private final ProjectionRepository projectionRepository;
    private final ProgramService programService;
    private final HallService hallService;
    private final MovieService movieService;

    public ProjectionService(
            dev.vdrenkov.cineledger.repositories.TicketRepository ticketRepository,
            ProjectionRepository projectionRepository,
            ProgramService programService,
            HallService hallService,
            MovieService movieService) {
        this.ticketRepository = ticketRepository;
        this.projectionRepository = projectionRepository;
        this.programService = programService;
        this.hallService = hallService;
        this.movieService = movieService;
    }

    @Transactional
    public Projection addProjection(final ProjectionRequest request) {
        final Hall hall = hallService.getHallForUpdate(request.hallId());
        final Program program = programService.getProgramForUpdate(request.programId());
        final Movie movie = movieService.getMovieById(request.movieId());
        validateSchedule(request, hall, program, movie.getRuntime(), 0);
        return projectionRepository.save(
                new Projection(request.price(), hall, program, movie, request.startTime()));
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

    public Projection getProjectionForUpdate(final int id) {
        return projectionRepository
                .findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ProjectionNotFoundException(
                                ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE));
    }

    public Projection getProjectionById(int id) {

        return projectionRepository
                .findById(id)
                .orElseThrow(() -> {
                    return new ProjectionNotFoundException(
                            ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE);
                });
    }

    public ProjectionDto getProjectionDtoById(int id) {

        return ProjectionMapper.mapProjectionToProjectionDto(getProjectionById(id));
    }

    @Transactional
    public ProjectionDto updateProjection(final ProjectionRequest request, final int id) {
        final Projection stored = getProjectionForUpdate(id);
        final ProjectionDto previous = ProjectionMapper.mapProjectionToProjectionDto(stored);
        final boolean identityChanged =
                request.hallId() != stored.getHall().getId()
                        || request.programId() != stored.getProgram().getId()
                        || request.movieId() != stored.getMovie().getId();

        if (identityChanged && ticketRepository.countByProjectionId(id) > 0) {
            throw new dev.vdrenkov.cineledger.exceptions.ResourceInUseException(
                    "A screening with issued tickets cannot change its movie, hall or program");
        }

        final Hall hall = hallService.getHallForUpdate(request.hallId());
        final Program program = programService.getProgramForUpdate(request.programId());
        final Movie movie = movieService.getMovieById(request.movieId());
        final var runtime =
                request.movieId() == stored.getMovie().getId()
                        ? stored.getRuntime()
                        : movie.getRuntime();
        validateSchedule(request, hall, program, runtime, id);
        stored.setPrice(request.price());
        stored.setHall(hall);
        stored.setProgram(program);

        if (request.movieId() != stored.getMovie().getId()) {
            stored.setMovie(movie);
        }

        stored.setStartTime(request.startTime());
        projectionRepository.save(stored);
        return previous;
    }

    public ProjectionDto deleteProjection(int id) {
        final ProjectionDto projectionDto = getProjectionDtoById(id);

        projectionRepository.deleteById(id);

        return projectionDto;
    }

    private void validateSchedule(
            final ProjectionRequest request,
            final Hall hall,
            final Program program,
            final java.time.Duration runtime,
            final int excludedId) {

        if (hall.getCinema().getId() != program.getCinema().getId()) {
            throw new IllegalArgumentException("The hall must belong to the program's cinema");
        }

        if (runtime.isZero() || runtime.isNegative()) {
            throw new IllegalArgumentException("Movie runtime must be positive");
        }

        final var startsAt = program.getProgramDate().atTime(request.startTime());
        final var endsAt = startsAt.plus(runtime);

        if (projectionRepository.overlaps(hall.getId(), excludedId, startsAt, endsAt)) {
            throw new HallNotAvailableException(ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION);
        }
    }
}
