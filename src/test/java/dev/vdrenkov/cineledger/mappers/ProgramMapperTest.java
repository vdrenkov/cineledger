package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.testutil.constants.ProgramConstants;
import dev.vdrenkov.cineledger.testutil.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutil.factories.ProgramFactory;
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
 * Tests program mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class ProgramMapperTest {

    @Mock
    private CinemaMapper cinemaMapper;
    @InjectMocks
    private ProgramMapper programMapper;

    /**
     * Verifies that map Program To Program DTO Test no Exceptions success.
     */
    @Test
    void testMapProgramToProgramDtoTest_noExceptions_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        final ProgramDto resultDto = programMapper.mapProgramToProgramDto(ProgramFactory.getDefaultProgram());

        Assertions.assertEquals(ProgramConstants.ID, resultDto.getId());
        Assertions.assertEquals(ProgramConstants.DATE, resultDto.getProgramDate());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getId(), resultDto.getCinema().getId());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getAddress(), resultDto.getCinema().getAddress());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getCity(), resultDto.getCinema().getCity());
    }

    /**
     * Verifies that map Program List To Program DTO List no Exceptions success.
     */
    @Test
    void testMapProgramListToProgramDtoList_noExceptions_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());
        List<ProgramDto> resultList = programMapper.mapProgramListToProgramDtoList(
            ProgramFactory.getDefaultProgramList());

        final ProgramDto resultDto = resultList.get(0);

        Assertions.assertEquals(ProgramConstants.ID, resultDto.getId());
        Assertions.assertEquals(ProgramConstants.DATE, resultDto.getProgramDate());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getId(), resultDto.getCinema().getId());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getAddress(), resultDto.getCinema().getAddress());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getCity(), resultDto.getCinema().getCity());
    }
}




