package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.testutil.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testutil.factories.ProjectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests projection mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class ProjectionMapperTest {

    @Mock
    private HallMapper hallMapper;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private ProgramMapper programMapper;

    @InjectMocks
    private ProjectionMapper projectionMapper;

    /**
     * Verifies that map Projection To Projection DTO no Exceptions success.
     */
    @Test
    void testMapProjectionToProjectionDto_noExceptions_success() {
        when(hallMapper.mapHallToHallDto(any())).thenReturn(ProjectionConstants.PROJECTION_HALL_DTO);
        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(ProjectionConstants.PROJECTION_MOVIE_DTO);
        when(programMapper.mapProgramToProgramDto(any())).thenReturn(ProjectionConstants.PROJECTION_PROGRAM_DTO);

        ProjectionDto resultDto = projectionMapper.mapProjectionToProjectionDto(
            ProjectionFactory.getDefaultProjection());

        Assertions.assertEquals(ProjectionConstants.ID, resultDto.getId());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.getId(), resultDto.getHall().getId());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.getCapacity(),
            resultDto.getHall().getCapacity());
        Assertions.assertEquals(ProjectionConstants.PRICE, resultDto.getPrice(), 0.0);
        Assertions.assertEquals(ProjectionConstants.PROJECTION_PROGRAM_DTO, resultDto.getProgram());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getId(), resultDto.getMovie().getId());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getTitle(), resultDto.getMovie().getTitle());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getDescription(),
            resultDto.getMovie().getDescription());
        Assertions.assertEquals(ProjectionConstants.START_TIME, resultDto.getStartTime());
    }

    /**
     * Verifies that map Projection List To Projection DTO List no Exceptions success.
     */
    @Test
    void testMapProjectionListToProjectionDtoList_noExceptions_success() {
        when(hallMapper.mapHallToHallDto(any())).thenReturn(ProjectionConstants.PROJECTION_HALL_DTO);
        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(ProjectionConstants.PROJECTION_MOVIE_DTO);
        when(programMapper.mapProgramToProgramDto(any())).thenReturn(ProjectionConstants.PROJECTION_PROGRAM_DTO);

        List<ProjectionDto> resultList = projectionMapper.mapProjectionListToProjectionDtoList(
            ProjectionFactory.getDefaultProjectionList());

        final ProjectionDto resultDto = resultList.getFirst();

        Assertions.assertEquals(ProjectionConstants.ID, resultDto.getId());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.getId(), resultDto.getHall().getId());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.getCapacity(),
            resultDto.getHall().getCapacity());
        Assertions.assertEquals(ProjectionConstants.PRICE, resultDto.getPrice(), 0.0);
        Assertions.assertEquals(ProjectionConstants.PROJECTION_PROGRAM_DTO, resultDto.getProgram());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getId(), resultDto.getMovie().getId());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getTitle(), resultDto.getMovie().getTitle());
        Assertions.assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getDescription(),
            resultDto.getMovie().getDescription());
        Assertions.assertEquals(ProjectionConstants.START_TIME, resultDto.getStartTime());
    }
}




