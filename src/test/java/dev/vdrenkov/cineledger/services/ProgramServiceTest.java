package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DateNotValidException;
import dev.vdrenkov.cineledger.exceptions.ProgramAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.ProgramNotFoundException;
import dev.vdrenkov.cineledger.mappers.ProgramMapper;
import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.models.entities.Program;
import dev.vdrenkov.cineledger.models.requests.ProgramRequest;
import dev.vdrenkov.cineledger.repositories.ProgramRepository;
import dev.vdrenkov.cineledger.testutils.constants.ProgramConstants;
import dev.vdrenkov.cineledger.testutils.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutils.factories.ProgramFactory;
import org.junit.jupiter.api.BeforeEach;
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

/**
 * Tests program service behavior.
 */
@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {
    ProgramRequest programRequest;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramMapper programMapper;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private ProgramService programService;

    @BeforeEach
    void setUp() {
        programRequest = ProgramFactory.getDefaultProgramRequest();
    }

    /**
     * Verifies that add Program no Exceptions success.
     */
    @Test
    void testAddProgram_noExceptions_success() {
        final Program expected = ProgramFactory.getDefaultProgram();

        when(programRepository.save(any())).thenReturn(expected);
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());

        final Program program = programService.addProgram(programRequest);

        assertEquals(expected, program);
    }

    /**
     * Verifies that add Program date Not Present throws Date Not Valid Exception.
     */
    @Test
    void testAddProgram_dateNotPresent_throwsDateNotValidException() {
        programRequest.setProgramDate(ProgramConstants.PAST_DATE);

        assertThrows(DateNotValidException.class, () -> programService.addProgram(programRequest));
    }

    /**
     * Verifies that add Program program Already Exists throws Program Already Exists Exception.
     */
    @Test
    void testAddProgram_programAlreadyExists_throwsProgramAlreadyExistsException() {
        when(programRepository.findByProgramDateAndCinemaId(any(), anyInt())).thenReturn(
            Optional.of(ProgramFactory.getDefaultProgram()));

        assertThrows(ProgramAlreadyExistsException.class, () -> programService.addProgram(programRequest));
    }

    /**
     * Verifies that get All Programs date Not Null success.
     */
    @Test
    void testGetAllPrograms_dateNotNull_success() {
        final List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(programMapper.mapProgramListToProgramDtoList(any())).thenReturn(ProgramFactory.getDefaultProgramDtoList());
        when(programRepository.findAllByProgramDate(any())).thenReturn(ProgramFactory.getDefaultProgramList());

        final List<ProgramDto> resultList = programService.getAllPrograms(ProgramConstants.DATE);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get All Programs date Null success.
     */
    @Test
    void testGetAllPrograms_dateNull_success() {
        final List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(programMapper.mapProgramListToProgramDtoList(any())).thenReturn(expected);
        when(programRepository.findAll()).thenReturn(ProgramFactory.getDefaultProgramList());

        final List<ProgramDto> resultList = programService.getAllPrograms(null);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Programs By Cinema Id cinema Found success.
     */
    @Test
    void testGetProgramsByCinemaId_cinemaFound_success() {
        final List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());
        when(programMapper.mapProgramListToProgramDtoList(any())).thenReturn(expected);
        when(programRepository.findAllByCinemaId(anyInt())).thenReturn(ProgramFactory.getDefaultProgramList());

        final List<ProgramDto> resultList = programService.getProgramsByCinemaId(ProgramConstants.ID);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Program By Id program Found success.
     */
    @Test
    void testGetProgramById_programFound_success() {
        final Program expected = ProgramFactory.getDefaultProgram();

        when(programRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Program program = programService.getProgramById(ProgramConstants.ID);

        assertEquals(expected, program);
    }

    /**
     * Verifies that get Program By Id program Not Found throws Program Not Found Exception.
     */
    @Test
    void testGetProgramById_programNotFound_throwsProgramNotFoundException() {
        assertThrows(ProgramNotFoundException.class, () -> programService.getProgramById(ProgramConstants.ID));
    }

    /**
     * Verifies that get Program DTO By Id program DTO Found success.
     */
    @Test
    void testGetProgramDtoById_programDtoFound_success() {
        final ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(expected);
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(new Program()));

        final ProgramDto program = programService.getProgramDtoById(ProgramConstants.ID);

        assertEquals(expected, program);
    }

    /**
     * Verifies that update Program program Updated success.
     */
    @Test
    void testUpdateProgram_programUpdated_success() {
        final ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(expected);
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));
        when(programRepository.save(any())).thenReturn(ProgramFactory.getDefaultProgram());

        ProgramDto program = programService.updateProgram(programRequest, ProgramConstants.ID);

        assertEquals(expected, program);

    }

    /**
     * Verifies that update Program date Not Present throws Date Not Valid Exception.
     */
    @Test
    void testUpdateProgram_dateNotPresent_throwsDateNotValidException() {
        programRequest.setProgramDate(ProgramConstants.PAST_DATE);

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(ProgramFactory.getDefaultProgramDto());
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

        assertThrows(DateNotValidException.class,
            () -> programService.updateProgram(programRequest, ProgramConstants.ID));
    }

    /**
     * Verifies that delete Program program Deleted success.
     */
    @Test
    void testDeleteProgram_programDeleted_success() {
        final ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(expected);
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

        final ProgramDto program = programService.deleteProgram(ProgramConstants.ID);

        assertEquals(expected, program);
    }

    /**
     * Verifies that is Date Not Valid.
     */
    @Test
    void testIsDateNotValid() {
        final boolean result = programService.isDateNotValid(LocalDate.of(2000, 1, 1));

        assertTrue(result);
    }
}



