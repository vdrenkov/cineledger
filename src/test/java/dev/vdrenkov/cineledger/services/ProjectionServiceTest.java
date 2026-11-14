package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import dev.vdrenkov.cineledger.exceptions.HallNotAvailableException;
import dev.vdrenkov.cineledger.exceptions.ProjectionNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.entities.Program;
import dev.vdrenkov.cineledger.models.entities.Projection;
import dev.vdrenkov.cineledger.models.requests.ProjectionRequest;
import dev.vdrenkov.cineledger.repositories.ProjectionRepository;
import dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testutils.factories.HallFactory;
import dev.vdrenkov.cineledger.testutils.factories.MovieFactory;
import dev.vdrenkov.cineledger.testutils.factories.ProgramFactory;
import dev.vdrenkov.cineledger.testutils.factories.ProjectionFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProjectionServiceTest {
    final ProjectionRequest projectionRequest = ProjectionFactory.getDefaultProjectionRequest();

    @Mock private ProjectionRepository projectionRepository;

    @Mock private HallService hallService;

    @Mock private ProgramService programService;

    @Mock private MovieService movieService;

    @InjectMocks private ProjectionService projectionService;

    @Test
    void testGetProjectionsByProgramId_programFound_success() {
        final Program program = ProgramFactory.getDefaultProgram();
        final List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(programService.getProgramById(anyInt())).thenReturn(program);
        when(projectionRepository.findProjectionsByProgramId(anyInt()))
                .thenReturn(ProjectionFactory.getDefaultProjectionList());

        final List<ProjectionDto> resultList =
                projectionService.getProjectionsByProgramId(program.getId());

        assertEquals(expected, resultList);
    }

    @Test
    void testGetProjectionByMovieId_movieFound_success() {
        final Movie movie = MovieFactory.getDefaultMovie();
        final List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(movieService.getMovieById(anyInt())).thenReturn(movie);
        when(projectionRepository.findProjectionsByMovieId(anyInt()))
                .thenReturn(ProjectionFactory.getDefaultProjectionList());

        final List<ProjectionDto> resultList =
                projectionService.getProjectionsByMovieId(movie.getId());

        assertEquals(expected, resultList);
    }

    @Test
    void testGetProjectionsByStartTime_isBeforeTrue_success() {
        final List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(projectionRepository.findProjectionsByStartTimeBefore(any()))
                .thenReturn(ProjectionFactory.getDefaultProjectionList());

        List<ProjectionDto> resultList =
                projectionService.getProjectionsByStartTime(ProjectionConstants.START_TIME, true);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetProjectionsByStartTime_isBeforeFalse_success() {
        final List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(projectionRepository.findProjectionsByStartTimeAfter(any()))
                .thenReturn(ProjectionFactory.getDefaultProjectionList());

        List<ProjectionDto> resultList =
                projectionService.getProjectionsByStartTime(ProjectionConstants.START_TIME, false);

        assertEquals(expected, resultList);
    }

    @Test
    void testAddProjection_noExceptions_success() {
        final Projection expected = ProjectionFactory.getDefaultProjection();

        when(projectionRepository.save(any())).thenReturn(expected);
        when(hallService.getHallForUpdate(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt()))
                .thenReturn(ProgramFactory.getDefaultProgram());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());

        final Projection projection = projectionService.addProjection(projectionRequest);

        assertEquals(expected, projection);
    }

    @Test
    void testAddProjection_hallNotAvailable_throwsHallNotAvailableException() {
        when(hallService.getHallForUpdate(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt()))
                .thenReturn(ProgramFactory.getDefaultProgram());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());

        when(projectionRepository.overlaps(anyInt(), anyInt(), any(), any())).thenReturn(true);

        assertThrows(
                HallNotAvailableException.class,
                () -> projectionService.addProjection(projectionRequest));
    }

    @Test
    void testUpdateProjection_projectionUpdated_success() {
        final ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();

        when(hallService.getHallForUpdate(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt()))
                .thenReturn(ProgramFactory.getDefaultProgram());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(projectionRepository.findByIdForUpdate(anyInt()))
                .thenReturn(Optional.of(ProjectionFactory.getDefaultProjection()));
        when(projectionRepository.save(any())).thenReturn(ProjectionFactory.getDefaultProjection());

        final ProjectionDto projectionDto =
                projectionService.updateProjection(projectionRequest, ProjectionConstants.ID);

        assertEquals(expected, projectionDto);
    }

    @Test
    void testUpdateProjection_hallNotAvailable_throwsHallNotAvailableException() {
        when(hallService.getHallForUpdate(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt()))
                .thenReturn(ProgramFactory.getDefaultProgram());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(projectionRepository.findByIdForUpdate(anyInt()))
                .thenReturn(Optional.of(ProjectionFactory.getDefaultProjection()));

        when(projectionRepository.overlaps(anyInt(), anyInt(), any(), any())).thenReturn(true);

        assertThrows(
                HallNotAvailableException.class,
                () ->
                        projectionService.updateProjection(
                                projectionRequest, ProjectionConstants.ID));
    }

    @Test
    void testDeleteProjection_projectionDeleted_success() {
        final ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();

        when(projectionRepository.findById(anyInt()))
                .thenReturn(Optional.of(ProjectionFactory.getDefaultProjection()));

        final ProjectionDto projectionDto =
                projectionService.deleteProjection(ProjectionConstants.ID);

        assertEquals(expected, projectionDto);
    }

    @Test
    void testDeleteProjection_projectionNotFound_throwsProjectionNotFoundException() {
        when(projectionRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(
                ProjectionNotFoundException.class,
                () -> projectionService.deleteProjection(ProjectionConstants.ID));
    }

    @Test
    void addProjection_crossesMidnight_checksFullDateTimeRange() {
        final var hall = HallFactory.getDefaultHall();
        final var program = ProgramFactory.getDefaultProgram();
        final var movie = MovieFactory.getDefaultMovie();
        final var request =
                new ProjectionRequest(
                        projectionRequest.price(),
                        hall.getId(),
                        program.getId(),
                        movie.getId(),
                        LocalTime.of(23, 0));
        when(hallService.getHallForUpdate(hall.getId())).thenReturn(hall);
        when(programService.getProgramById(program.getId())).thenReturn(program);
        when(movieService.getMovieById(movie.getId())).thenReturn(movie);
        when(projectionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        projectionService.addProjection(request);

        final var startsAt = program.getProgramDate().atTime(23, 0);
        org.mockito.Mockito.verify(projectionRepository)
                .overlaps(hall.getId(), 0, startsAt, startsAt.plus(movie.getRuntime()));
    }
}
