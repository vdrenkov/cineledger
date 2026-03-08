package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.testutils.constants.ProgramConstants;
import dev.vdrenkov.cineledger.testutils.factories.ProgramFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests program mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class ProgramMapperTest {
    /**
     * Verifies that map Program To Program DTO Test no Exceptions success.
     */
    @Test
    void testMapProgramToProgramDtoTest_noExceptions_success() {
        final ProgramDto resultDto = ProgramMapper.mapProgramToProgramDto(ProgramFactory.getDefaultProgram());

        assertEquals(ProgramConstants.ID, resultDto.getId());
        assertEquals(ProgramConstants.DATE, resultDto.getProgramDate());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getId(), resultDto.getCinema().getId());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getAddress(), resultDto.getCinema().getAddress());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getCity(), resultDto.getCinema().getCity());
    }

    /**
     * Verifies that map Program List To Program DTO List no Exceptions success.
     */
    @Test
    void testMapProgramListToProgramDtoList_noExceptions_success() {
        final List<ProgramDto> resultList = ProgramMapper.mapProgramListToProgramDtoList(
            ProgramFactory.getDefaultProgramList());

        final ProgramDto resultDto = resultList.getFirst();

        assertEquals(ProgramConstants.ID, resultDto.getId());
        assertEquals(ProgramConstants.DATE, resultDto.getProgramDate());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getId(), resultDto.getCinema().getId());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getAddress(), resultDto.getCinema().getAddress());
        assertEquals(ProgramConstants.PROGRAM_CINEMA.getCity(), resultDto.getCinema().getCity());
    }
}




