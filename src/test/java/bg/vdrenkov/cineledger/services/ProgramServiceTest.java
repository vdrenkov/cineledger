package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.DateNotValidException;
import bg.vdrenkov.cineledger.exceptions.ProgramAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.ProgramNotFoundException;
import bg.vdrenkov.cineledger.mappers.ProgramMapper;
import bg.vdrenkov.cineledger.models.dtos.ProgramDto;
import bg.vdrenkov.cineledger.models.entities.Program;
import bg.vdrenkov.cineledger.models.requests.ProgramRequest;
import bg.vdrenkov.cineledger.repositories.ProgramRepository;
import bg.vdrenkov.cineledger.testUtils.constants.ProgramConstants;
import bg.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
import bg.vdrenkov.cineledger.testUtils.factories.ProgramFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramMapper programMapper;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private ProgramService programService;

    @Test
    public void testAddProgram_noExceptions_success() {
        Program expected = ProgramFactory.getDefaultProgram();

        when(programRepository.save(any())).thenReturn(expected);
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());

        Program program = programService.addProgram(ProgramFactory.getDefaultProgramRequest());

        assertEquals(expected, program);
    }

    @Test
    public void testAddProgram_dateNotPresent_throwsDateNotValidException() {
        assertThrows(DateNotValidException.class, () -> {

            ProgramRequest request = ProgramFactory.getDefaultProgramRequest();
            request.setProgramDate(ProgramConstants.PAST_DATE);

            programService.addProgram(request);

        });
    }

    @Test
    public void testAddProgram_programAlreadyExists_throwsProgramAlreadyExistsException() {
        assertThrows(ProgramAlreadyExistsException.class, () -> {

            when(programRepository.findByProgramDateAndCinemaId(any(), anyInt())).thenReturn(
                Optional.of(ProgramFactory.getDefaultProgram()));

            programService.addProgram(ProgramFactory.getDefaultProgramRequest());

        });
    }

    @Test
    public void testGetAllPrograms_dateNotNull_success() {
        List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(programMapper.mapProgramListToProgramDtoList(any())).thenReturn(ProgramFactory.getDefaultProgramDtoList());
        when(programRepository.findAllByProgramDate(any())).thenReturn(ProgramFactory.getDefaultProgramList());

        List<ProgramDto> resultList = programService.getAllPrograms(ProgramConstants.DATE);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetAllPrograms_dateNull_success() {
        List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(programMapper.mapProgramListToProgramDtoList(any())).thenReturn(expected);
        when(programRepository.findAll()).thenReturn(ProgramFactory.getDefaultProgramList());

        List<ProgramDto> resultList = programService.getAllPrograms(null);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetProgramsByCinemaId_cinemaFound_success() {
        List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());
        when(programMapper.mapProgramListToProgramDtoList(any())).thenReturn(expected);
        when(programRepository.findAllByCinemaId(anyInt())).thenReturn(ProgramFactory.getDefaultProgramList());

        List<ProgramDto> resultList = programService.getProgramsByCinemaId(ProgramConstants.ID);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetProgramById_programFound_success() {
        Program expected = ProgramFactory.getDefaultProgram();

        when(programRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Program program = programService.getProgramById(ProgramConstants.ID);

        assertEquals(expected, program);
    }

    @Test
    public void testGetProgramById_programNotFound_throwsProgramNotFoundException() {
        assertThrows(ProgramNotFoundException.class, () -> {

            programService.getProgramById(ProgramConstants.ID);

        });
    }

    @Test
    public void testGetProgramDtoById_programDtoFound_success() {
        ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(expected);
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(new Program()));

        ProgramDto program = programService.getProgramDtoById(ProgramConstants.ID);

        assertEquals(expected, program);
    }

    @Test
    public void testUpdateProgram_programUpdated_success() {
        ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(expected);
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));
        when(programRepository.save(any())).thenReturn(ProgramFactory.getDefaultProgram());

        ProgramDto program = programService.updateProgram(ProgramFactory.getDefaultProgramRequest(),
            ProgramConstants.ID);

        assertEquals(expected, program);

    }

    @Test
    public void testUpdateProgram_dateNotPresent_throwsDateNotValidException() {
        assertThrows(DateNotValidException.class, () -> {

            ProgramRequest request = ProgramFactory.getDefaultProgramRequest();
            request.setProgramDate(ProgramConstants.PAST_DATE);

            when(programMapper.mapProgramToProgramDto(any())).thenReturn(ProgramFactory.getDefaultProgramDto());
            when(programRepository.findById(anyInt())).thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

            programService.updateProgram(request, ProgramConstants.ID);

        });
    }

    @Test
    public void testDeleteProgram_programDeleted_success() {
        ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(expected);
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

        ProgramDto program = programService.deleteProgram(ProgramConstants.ID);

        assertEquals(expected, program);
    }

    @Test
    public void testIsDateNotValid() {
        boolean result = programService.isDateNotValid(LocalDate.of(2000, 1, 1));

        assertTrue(result);
    }
}



