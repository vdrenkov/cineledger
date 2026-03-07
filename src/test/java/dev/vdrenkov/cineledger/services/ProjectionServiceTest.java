package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.HallNotAvailableException;
import dev.vdrenkov.cineledger.exceptions.ProgramNotFoundException;
import dev.vdrenkov.cineledger.mappers.ProjectionMapper;
import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.entities.Program;
import dev.vdrenkov.cineledger.models.entities.Projection;
import dev.vdrenkov.cineledger.repositories.ProjectionRepository;
import dev.vdrenkov.cineledger.testUtils.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testUtils.factories.HallFactory;
import dev.vdrenkov.cineledger.testUtils.factories.MovieFactory;
import dev.vdrenkov.cineledger.testUtils.factories.ProgramFactory;
import dev.vdrenkov.cineledger.testUtils.factories.ProjectionFactory;
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

@ExtendWith(MockitoExtension.class)
public class ProjectionServiceTest {

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

    @Test
    public void testGetProjectionsByProgramId_programFound_success() {
        Program program = ProgramFactory.getDefaultProgram();
        List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(programService.getProgramById(anyInt())).thenReturn(program);
        when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
        when(projectionRepository.findProjectionsByProgramId(anyInt())).thenReturn(
            ProjectionFactory.getDefaultProjectionList());

        List<ProjectionDto> resultList = projectionService.getProjectionsByProgramId(program.getId());

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetProjectionByMovieId_movieFound_success() {
        Movie movie = MovieFactory.getDefaultMovie();
        List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(movieService.getMovieById(anyInt())).thenReturn(movie);
        when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
        when(projectionRepository.findProjectionsByMovieId(anyInt())).thenReturn(
            ProjectionFactory.getDefaultProjectionList());

        List<ProjectionDto> resultList = projectionService.getProjectionsByMovieId(movie.getId());

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetProjectionsByStartTime_isBeforeTrue_success() {
        List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
        when(projectionRepository.findProjectionsByStartTimeBefore(any())).thenReturn(
            ProjectionFactory.getDefaultProjectionList());

        List<ProjectionDto> resultList = projectionService.getProjectionsByStartTime(ProjectionConstants.START_TIME,
            true);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetProjectionsByStartTime_isBeforeFalse_success() {
        List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

        when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
        when(projectionRepository.findProjectionsByStartTimeAfter(any())).thenReturn(
            ProjectionFactory.getDefaultProjectionList());

        List<ProjectionDto> resultList = projectionService.getProjectionsByStartTime(ProjectionConstants.START_TIME,
            false);

        assertEquals(expected, resultList);
    }

    @Test
    public void testAddProjection_noExceptions_success() {
        Projection expected = ProjectionFactory.getDefaultProjection();

        when(projectionRepository.save(any())).thenReturn(expected);
        when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());

        Projection projection = projectionService.addProjection(ProjectionFactory.getDefaultProjectionRequest());

        assertEquals(expected, projection);
    }

    @Test
    public void testAddProjection_hallNotAvailable_throwsHallNotAvailableException() {
        assertThrows(HallNotAvailableException.class, () -> {

            when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
            when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());

            ProjectionService spyProjectionService = Mockito.spy(projectionService);
            doReturn(false).when(spyProjectionService).isHallAvailable(anyInt(), anyInt(), any(LocalTime.class));

            spyProjectionService.addProjection(ProjectionFactory.getDefaultProjectionRequest());

        });
    }

    @Test
    public void testUpdateProjection_projectionUpdated_success() {
        ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();

        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(expected);
        when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
        when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());
        when(projectionRepository.findById(anyInt())).thenReturn(Optional.of(ProjectionFactory.getDefaultProjection()));
        when(projectionRepository.save(any())).thenReturn(ProjectionFactory.getDefaultProjection());

        ProjectionDto projectionDto = projectionService.updateProjection(
            ProjectionFactory.getDefaultProjectionRequest(), ProjectionConstants.ID);

        assertEquals(expected, projectionDto);
    }

    @Test
    public void testUpdateProjection_hallNotAvailable_throwsHallNotAvailableException() {
        assertThrows(HallNotAvailableException.class, () -> {

            ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();
            when(projectionMapper.mapProjectionToProjectionDto(any(Projection.class))).thenReturn(expected);
            when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
            when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());
            when(projectionRepository.findById(anyInt())).thenReturn(
                Optional.of(ProjectionFactory.getDefaultProjection()));

            ProjectionService spyProjectionService = Mockito.spy(projectionService);
            doReturn(false).when(spyProjectionService).isHallAvailable(anyInt(), anyInt(), any(LocalTime.class));

            spyProjectionService.updateProjection(ProjectionFactory.getDefaultProjectionRequest(),
                ProjectionConstants.ID);

        });
    }

    @Test
    public void testDeleteProjection_projectionDeleted_success() {
        ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();

        when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(expected);
        when(projectionRepository.findById(anyInt())).thenReturn(Optional.of(new Projection()));

        ProjectionDto projectionDto = projectionService.deleteProjection(ProjectionConstants.ID);

        assertEquals(expected, projectionDto);
    }

    @Test
    public void testDeleteProjection_projectionNotFound_throwsProgramNotFoundException() {
        assertThrows(ProgramNotFoundException.class, () -> {

            when(projectionRepository.findById(anyInt())).thenReturn(Optional.empty());

            projectionService.deleteProjection(ProjectionConstants.ID);

        });
    }

    @Test
    public void testIsHallAvailableInPeriod_overlappingProjection_returnsFalse() {
        int hallId = 1;
        int programId = 1;
        LocalTime startTime = LocalTime.of(21, 0);

        Projection overlappingProjection = new Projection();
        overlappingProjection.setStartTime(LocalTime.of(23, 0));
        Program program = new Program();
        program.setId(programId);
        overlappingProjection.setProgram(program);

        when(projectionRepository.findProjectionsByHallIdAndStartTimeBetween(anyInt(), any(), any())).thenReturn(
            Collections.singletonList(overlappingProjection));

        boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

        assertFalse(isAvailable);
    }

    @Test
    public void testIsHallAvailableInPeriod_noOverlappingProjection_returnsTrue() {
        int hallId = 1;
        int programId = 1;
        LocalTime startTime = LocalTime.of(10, 0);

        boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

        assertTrue(isAvailable);
    }

    @Test
    public void testIsHallAvailable_crossMidnight_overlappingProjectionInMorning_returnsFalse() {
        int hallId = 1;
        int programId = 1;
        LocalTime startTime = LocalTime.of(23, 0);

        Projection overlappingProjection = new Projection();
        overlappingProjection.setStartTime(LocalTime.of(1, 0));
        Program program = new Program();
        program.setId(programId);
        overlappingProjection.setProgram(program);

        when(projectionRepository.findProjectionsByHallIdAndStartTimeBetween(anyInt(), any(), any())).thenReturn(
            Collections.emptyList());
        when(projectionRepository.findProjectionsByHallIdAndStartTimeBetween(anyInt(), any(), any())).thenReturn(
            Collections.singletonList(overlappingProjection));

        boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

        assertFalse(isAvailable);
    }

    @Test
    public void testIsHallAvailable_crossMidnight_noOverlappingProjection_returnsTrue() {
        int hallId = 1;
        int programId = 1;
        LocalTime startTime = LocalTime.of(23, 0);

        boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

        assertTrue(isAvailable);
    }
}




