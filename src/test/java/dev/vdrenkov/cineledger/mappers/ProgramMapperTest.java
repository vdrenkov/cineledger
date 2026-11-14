package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.testutils.constants.ProgramConstants;
import dev.vdrenkov.cineledger.testutils.factories.ProgramFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProgramMapperTest {

    @Test
    void testMapProgramToProgramDtoTest_noExceptions_success() {
        final ProgramDto resultDto = ProgramMapper.mapProgramToProgramDto(ProgramFactory.getDefaultProgram());

        assertEquals(ProgramConstants.ID, resultDto.id());
        assertEquals(ProgramConstants.DATE, resultDto.programDate());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getId(), resultDto.cinema().id());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getAddress(), resultDto.cinema().address());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getCity(), resultDto.cinema().city());
    }

    @Test
    void testMapProgramListToProgramDtoList_noExceptions_success() {
        final List<ProgramDto> resultList = ProgramMapper.mapProgramListToProgramDtoList(
            ProgramFactory.getDefaultProgramList());

        final ProgramDto resultDto = resultList.getFirst();

        assertEquals(ProgramConstants.ID, resultDto.id());
        assertEquals(ProgramConstants.DATE, resultDto.programDate());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getId(), resultDto.cinema().id());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getAddress(), resultDto.cinema().address());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getCity(), resultDto.cinema().city());
    }
}
