package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testutils.factories.ProjectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests projection mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class ProjectionMapperTest {
    /**
     * Verifies that map Projection To Projection DTO no Exceptions success.
     */
    @Test
    void testMapProjectionToProjectionDto_noExceptions_success() {
        final ProjectionDto resultDto = ProjectionMapper.mapProjectionToProjectionDto(
            ProjectionFactory.getDefaultProjection());

        assertEquals(ProjectionConstants.ID, resultDto.getId());
        assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.getId(), resultDto.getHall().getId());
        assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.getCapacity(), resultDto.getHall().getCapacity());
        assertEquals(ProjectionConstants.PRICE, resultDto.getPrice(), 0.0);
        assertEquals(ProjectionConstants.PROJECTION_PROGRAM_DTO, resultDto.getProgram());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getId(), resultDto.getMovie().getId());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getTitle(), resultDto.getMovie().getTitle());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getDescription(), resultDto.getMovie().getDescription());
        assertEquals(ProjectionConstants.START_TIME, resultDto.getStartTime());
    }

    /**
     * Verifies that map Projection List To Projection DTO List no Exceptions success.
     */
    @Test
    void testMapProjectionListToProjectionDtoList_noExceptions_success() {
        final List<ProjectionDto> resultList = ProjectionMapper.mapProjectionListToProjectionDtoList(
            ProjectionFactory.getDefaultProjectionList());

        final ProjectionDto resultDto = resultList.getFirst();

        assertEquals(ProjectionConstants.ID, resultDto.getId());
        assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.getId(), resultDto.getHall().getId());
        assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.getCapacity(), resultDto.getHall().getCapacity());
        assertEquals(ProjectionConstants.PRICE, resultDto.getPrice(), 0.0);
        assertEquals(ProjectionConstants.PROJECTION_PROGRAM_DTO, resultDto.getProgram());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getId(), resultDto.getMovie().getId());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getTitle(), resultDto.getMovie().getTitle());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.getDescription(), resultDto.getMovie().getDescription());
        assertEquals(ProjectionConstants.START_TIME, resultDto.getStartTime());
    }
}




