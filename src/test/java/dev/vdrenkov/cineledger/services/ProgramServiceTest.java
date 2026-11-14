package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import dev.vdrenkov.cineledger.exceptions.DateNotValidException;
import dev.vdrenkov.cineledger.exceptions.ProgramAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.ProgramNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.models.entities.Program;
import dev.vdrenkov.cineledger.models.requests.ProgramRequest;
import dev.vdrenkov.cineledger.repositories.ProgramRepository;
import dev.vdrenkov.cineledger.repositories.ProjectionRepository;
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

@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {
    ProgramRequest programRequest;

    @Mock private ProgramRepository programRepository;

    @Mock private CinemaService cinemaService;

    @Mock private ProjectionRepository projectionRepository;

    @InjectMocks private ProgramService programService;

    @BeforeEach
    void setUp() {
        programRequest = ProgramFactory.getDefaultProgramRequest();
    }

    @Test
    void testAddProgram_noExceptions_success() {
        final Program expected = ProgramFactory.getDefaultProgram();

        when(programRepository.save(any())).thenReturn(expected);
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());

        final Program program = programService.addProgram(programRequest);

        assertEquals(expected, program);
    }

    @Test
    void testAddProgram_dateNotPresent_throwsDateNotValidException() {
        programRequest = new ProgramRequest(ProgramConstants.PAST_DATE, programRequest.cinemaId());

        assertThrows(DateNotValidException.class, () -> programService.addProgram(programRequest));
    }

    @Test
    void testAddProgram_programAlreadyExists_throwsProgramAlreadyExistsException() {
        when(programRepository.findByProgramDateAndCinemaId(any(), anyInt()))
                .thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

        assertThrows(
                ProgramAlreadyExistsException.class,
                () -> programService.addProgram(programRequest));
    }

    @Test
    void testGetAllPrograms_dateNotNull_success() {
        final List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(programRepository.findAllByProgramDate(any()))
                .thenReturn(ProgramFactory.getDefaultProgramList());

        final List<ProgramDto> resultList = programService.getAllPrograms(ProgramConstants.DATE);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetAllPrograms_dateNull_success() {
        final List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(programRepository.findAll()).thenReturn(ProgramFactory.getDefaultProgramList());

        final List<ProgramDto> resultList = programService.getAllPrograms(null);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetProgramsByCinemaId_cinemaFound_success() {
        final List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());
        when(programRepository.findAllByCinemaId(anyInt()))
                .thenReturn(ProgramFactory.getDefaultProgramList());

        final List<ProgramDto> resultList =
                programService.getProgramsByCinemaId(ProgramConstants.ID);

        assertEquals(expected, resultList);
    }

    @Test
    void testGetProgramById_programFound_success() {
        final Program expected = ProgramFactory.getDefaultProgram();

        when(programRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Program program = programService.getProgramById(ProgramConstants.ID);

        assertEquals(expected, program);
    }

    @Test
    void testGetProgramById_programNotFound_throwsProgramNotFoundException() {
        assertThrows(
                ProgramNotFoundException.class,
                () -> programService.getProgramById(ProgramConstants.ID));
    }

    @Test
    void testGetProgramDtoById_programDtoFound_success() {
        final ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programRepository.findById(anyInt()))
                .thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

        final ProgramDto program = programService.getProgramDtoById(ProgramConstants.ID);

        assertEquals(expected, program);
    }

    @Test
    void testUpdateProgram_programUpdated_success() {
        final ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programRepository.findByIdForUpdate(anyInt()))
                .thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());
        when(programRepository.save(any())).thenReturn(ProgramFactory.getDefaultProgram());

        final ProgramDto program =
                programService.updateProgram(programRequest, ProgramConstants.ID);

        assertEquals(expected, program);
    }

    @Test
    void testUpdateProgram_dateNotPresent_throwsDateNotValidException() {
        programRequest = new ProgramRequest(ProgramConstants.PAST_DATE, programRequest.cinemaId());

        when(programRepository.findByIdForUpdate(anyInt()))
                .thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

        assertThrows(
                DateNotValidException.class,
                () -> programService.updateProgram(programRequest, ProgramConstants.ID));
    }

    @Test
    void testDeleteProgram_programDeleted_success() {
        final ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programRepository.findById(anyInt()))
                .thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

        final ProgramDto program = programService.deleteProgram(ProgramConstants.ID);

        assertEquals(expected, program);
    }

    @Test
    void testIsDateNotValid() {
        final boolean result = programService.isDateNotValid(LocalDate.of(2000, 1, 1));

        assertTrue(result);
    }
}
