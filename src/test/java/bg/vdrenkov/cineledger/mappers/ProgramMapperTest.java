package bg.vdrenkov.cineledger.mappers;

import bg.vdrenkov.cineledger.models.dtos.ProgramDto;
import bg.vdrenkov.cineledger.testUtils.constants.ProgramConstants;
import bg.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
import bg.vdrenkov.cineledger.testUtils.factories.ProgramFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProgramMapperTest {

    @Mock
    private CinemaMapper cinemaMapper;
    @InjectMocks
    private ProgramMapper programMapper;

    @Test
    public void testMapProgramToProgramDtoTest_noExceptions_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        ProgramDto resultDto = programMapper.mapProgramToProgramDto(ProgramFactory.getDefaultProgram());

        Assertions.assertEquals(ProgramConstants.ID, resultDto.getId());
        Assertions.assertEquals(ProgramConstants.DATE, resultDto.getProgramDate());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getId(), resultDto.getCinema().getId());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getAddress(), resultDto.getCinema().getAddress());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getCity(), resultDto.getCinema().getCity());
    }

    @Test
    public void testMapProgramListToProgramDtoList_noExceptions_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());
        List<ProgramDto> resultList = programMapper.mapProgramListToProgramDtoList(
            ProgramFactory.getDefaultProgramList());

        ProgramDto resultDto = resultList.get(0);

        Assertions.assertEquals(ProgramConstants.ID, resultDto.getId());
        Assertions.assertEquals(ProgramConstants.DATE, resultDto.getProgramDate());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getId(), resultDto.getCinema().getId());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getAddress(), resultDto.getCinema().getAddress());
        Assertions.assertEquals(ProgramConstants.PROGRAM_CINEMA.getCity(), resultDto.getCinema().getCity());
    }
}




