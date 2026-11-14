package dev.vdrenkov.cineledger.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testutils.factories.ProjectionFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProjectionMapperTest {

    @Test
    void testMapProjectionToProjectionDto_noExceptions_success() {
        final ProjectionDto resultDto =
                ProjectionMapper.mapProjectionToProjectionDto(
                        ProjectionFactory.getDefaultProjection());

        assertEquals(ProjectionConstants.ID, resultDto.id());
        assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.id(), resultDto.hall().id());
        assertEquals(
                ProjectionConstants.PROJECTION_HALL_DTO.capacity(), resultDto.hall().capacity());
        assertEquals(ProjectionConstants.PRICE, resultDto.price());
        assertEquals(ProjectionConstants.PROJECTION_PROGRAM_DTO, resultDto.program());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.id(), resultDto.movie().id());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.title(), resultDto.movie().title());
        assertEquals(
                ProjectionConstants.PROJECTION_MOVIE_DTO.description(),
                resultDto.movie().description());
        assertEquals(ProjectionConstants.START_TIME, resultDto.startTime());
    }

    @Test
    void testMapProjectionListToProjectionDtoList_noExceptions_success() {
        final List<ProjectionDto> resultList =
                ProjectionMapper.mapProjectionListToProjectionDtoList(
                        ProjectionFactory.getDefaultProjectionList());

        final ProjectionDto resultDto = resultList.getFirst();

        assertEquals(ProjectionConstants.ID, resultDto.id());
        assertEquals(ProjectionConstants.PROJECTION_HALL_DTO.id(), resultDto.hall().id());
        assertEquals(
                ProjectionConstants.PROJECTION_HALL_DTO.capacity(), resultDto.hall().capacity());
        assertEquals(ProjectionConstants.PRICE, resultDto.price());
        assertEquals(ProjectionConstants.PROJECTION_PROGRAM_DTO, resultDto.program());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.id(), resultDto.movie().id());
        assertEquals(ProjectionConstants.PROJECTION_MOVIE_DTO.title(), resultDto.movie().title());
        assertEquals(
                ProjectionConstants.PROJECTION_MOVIE_DTO.description(),
                resultDto.movie().description());
        assertEquals(ProjectionConstants.START_TIME, resultDto.startTime());
    }
}
