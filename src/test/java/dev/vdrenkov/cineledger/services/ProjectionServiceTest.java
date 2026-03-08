package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.HallNotAvailableException;
import dev.vdrenkov.cineledger.exceptions.ProgramNotFoundException;
import dev.vdrenkov.cineledger.mappers.ProjectionMapper;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Tests projection service behavior.
 */
@ExtendWith(MockitoExtension.class)
class ProjectionServiceTest {
    final ProjectionRequest projectionRequest = ProjectionFactory.getDefaultProjectionRequest();

    @Mock
    private ProjectionRepository projectionRepository;

    @Mock
    private ProjectionMapper projectionMapper;

    @Mock
    private HallService hallService;

    @Mock
    private ProgramService programService;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private ProjectionService projectionService;

    /**
     * Verifies that get Projections By Program Id program Found success.
     */
    @Test
    void testGetProjectionsByProgramId_programFound_success() {
        final Program program = ProgramFactory.getDefaultProgram();
        final List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(programService.getProgramById(anyInt())).thenReturn(program);
        when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
        when(projectionRepository.findProjectionsByProgramId(anyInt())).thenReturn(
            ProjectionFactory.getDefaultProjectionList());

        final List<ProjectionDto> resultList = projectionService.getProjectionsByProgramId(program.getId());

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Projection By Movie Id movie Found success.
     */
    @Test
    void testGetProjectionByMovieId_movieFound_success() {
        final Movie movie = MovieFactory.getDefaultMovie();
        final List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(movieService.getMovieById(anyInt())).thenReturn(movie);
        when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
        when(projectionRepository.findProjectionsByMovieId(anyInt())).thenReturn(
            ProjectionFactory.getDefaultProjectionList());

        final List<ProjectionDto> resultList = projectionService.getProjectionsByMovieId(movie.getId());

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Projections By Start Time is Before True success.
     */
    @Test
    void testGetProjectionsByStartTime_isBeforeTrue_success() {
        final List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
        when(projectionRepository.findProjectionsByStartTimeBefore(any())).thenReturn(
            ProjectionFactory.getDefaultProjectionList());

        List<ProjectionDto> resultList = projectionService.getProjectionsByStartTime(ProjectionConstants.START_TIME,
            true);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Projections By Start Time is Before False success.
     */
    @Test
    void testGetProjectionsByStartTime_isBeforeFalse_success() {
        final List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
        when(projectionRepository.findProjectionsByStartTimeAfter(any())).thenReturn(
            ProjectionFactory.getDefaultProjectionList());

        List<ProjectionDto> resultList = projectionService.getProjectionsByStartTime(ProjectionConstants.START_TIME,
            false);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that add Projection no Exceptions success.
     */
    @Test
    void testAddProjection_noExceptions_success() {
        final Projection expected = ProjectionFactory.getDefaultProjection();

        when(projectionRepository.save(any())).thenReturn(expected);
        when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());

        final Projection projection = projectionService.addProjection(projectionRequest);

        assertEquals(expected, projection);
    }

    /**
     * Verifies that add Projection hall Not Available throws Hall Not Available Exception.
     */
    @Test
    void testAddProjection_hallNotAvailable_throwsHallNotAvailableException() {
        when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());

        final ProjectionService spyProjectionService = Mockito.spy(projectionService);
        doReturn(false).when(spyProjectionService).isHallAvailable(anyInt(), anyInt(), any(LocalTime.class));

        assertThrows(HallNotAvailableException.class, () -> spyProjectionService.addProjection(projectionRequest));
    }

    /**
     * Verifies that update Projection projection Updated success.
     */
    @Test
    void testUpdateProjection_projectionUpdated_success() {
        final ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();

        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(expected);
        when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());
        when(projectionRepository.findById(anyInt())).thenReturn(Optional.of(ProjectionFactory.getDefaultProjection()));
        when(projectionRepository.save(any())).thenReturn(ProjectionFactory.getDefaultProjection());

        ProjectionDto projectionDto = projectionService.updateProjection(projectionRequest, ProjectionConstants.ID);

        assertEquals(expected, projectionDto);
    }

    /**
     * Verifies that update Projection hall Not Available throws Hall Not Available Exception.
     */
    @Test
    void testUpdateProjection_hallNotAvailable_throwsHallNotAvailableException() {
        final ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();
        when(projectionMapper.mapProjectionToProjectionDto(any(Projection.class))).thenReturn(expected);
        when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());
        when(projectionRepository.findById(anyInt())).thenReturn(Optional.of(ProjectionFactory.getDefaultProjection()));

        final ProjectionService spyProjectionService = Mockito.spy(projectionService);
        doReturn(false).when(spyProjectionService).isHallAvailable(anyInt(), anyInt(), any(LocalTime.class));

        assertThrows(HallNotAvailableException.class,
            () -> spyProjectionService.updateProjection(projectionRequest, ProjectionConstants.ID));
    }

    /**
     * Verifies that delete Projection projection Deleted success.
     */
    @Test
    void testDeleteProjection_projectionDeleted_success() {
        final ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();

        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(expected);
        when(projectionRepository.findById(anyInt())).thenReturn(Optional.of(new Projection()));

        final ProjectionDto projectionDto = projectionService.deleteProjection(ProjectionConstants.ID);

        assertEquals(expected, projectionDto);
    }

    /**
     * Verifies that delete Projection projection Not Found throws Program Not Found Exception.
     */
    @Test
    void testDeleteProjection_projectionNotFound_throwsProgramNotFoundException() {
        when(projectionRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProgramNotFoundException.class, () -> projectionService.deleteProjection(ProjectionConstants.ID));
    }

    /**
     * Verifies that is Hall Available In Period overlapping Projection returns False.
     */
    @Test
    void testIsHallAvailableInPeriod_overlappingProjection_returnsFalse() {
        final int hallId = 1;
        final int programId = 1;
        final LocalTime startTime = LocalTime.of(21, 0);

        final Projection overlappingProjection = new Projection();
        overlappingProjection.setStartTime(LocalTime.of(23, 0));
        final Program program = new Program();
        program.setId(programId);
        overlappingProjection.setProgram(program);

        when(projectionRepository.findProjectionsByHallIdAndStartTimeBetween(anyInt(), any(), any())).thenReturn(
            Collections.singletonList(overlappingProjection));

        final boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

        assertFalse(isAvailable);
    }

    /**
     * Verifies that is Hall Available In Period no Overlapping Projection returns True.
     */
    @Test
    void testIsHallAvailableInPeriod_noOverlappingProjection_returnsTrue() {
        final int hallId = 1;
        final int programId = 1;
        final LocalTime startTime = LocalTime.of(10, 0);

        final boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

        assertTrue(isAvailable);
    }

    /**
     * Verifies that is Hall Available cross Midnight overlapping Projection In Morning returns False.
     */
    @Test
    void testIsHallAvailable_crossMidnight_overlappingProjectionInMorning_returnsFalse() {
        final int hallId = 1;
        final int programId = 1;
        final LocalTime startTime = LocalTime.of(23, 0);

        final Projection overlappingProjection = new Projection();
        overlappingProjection.setStartTime(LocalTime.of(1, 0));
        final Program program = new Program();
        program.setId(programId);
        overlappingProjection.setProgram(program);

        when(projectionRepository.findProjectionsByHallIdAndStartTimeBetween(anyInt(), any(), any())).thenReturn(
            Collections.emptyList());
        when(projectionRepository.findProjectionsByHallIdAndStartTimeBetween(anyInt(), any(), any())).thenReturn(
            Collections.singletonList(overlappingProjection));

        final boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

        assertFalse(isAvailable);
    }

    /**
     * Verifies that is Hall Available cross Midnight no Overlapping Projection returns True.
     */
    @Test
    void testIsHallAvailable_crossMidnight_noOverlappingProjection_returnsTrue() {
        final int hallId = 1;
        final int programId = 1;
        final LocalTime startTime = LocalTime.of(23, 0);

        final boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

        assertTrue(isAvailable);
    }
}




