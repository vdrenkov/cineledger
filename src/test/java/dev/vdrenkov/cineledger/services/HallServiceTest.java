package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.HallNotFoundException;
import dev.vdrenkov.cineledger.mappers.HallMapper;
import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.repositories.HallRepository;
import dev.vdrenkov.cineledger.testutil.constants.HallConstants;
import dev.vdrenkov.cineledger.testutil.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutil.factories.HallFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Tests hall service behavior.
 */
@ExtendWith(MockitoExtension.class)
class HallServiceTest {

    @Mock
    private HallRepository hallRepository;

    @Mock
    private HallMapper hallMapper;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private HallService hallService;

    /**
     * Verifies that add Hall no Exceptions success.
     */
    @Test
    void testAddHall_noExceptions_success() {
        final Hall expected = HallFactory.getDefaultHall();
        when(hallRepository.save(any())).thenReturn(expected);
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());

        final Hall hall = hallService.addHall(HallFactory.getDefaultHallRequest());

        assertEquals(expected, hall);
    }

    /**
     * Verifies that get Hall By Cinema Id cinema Found success.
     */
    @Test
    void testGetHallByCinemaId_cinemaFound_success() {
        final Cinema cinema = CinemaFactory.getDefaultCinema();
        final List<HallDto> expected = HallFactory.getDefaultHallDtoList();

        when(cinemaService.getCinemaById(anyInt())).thenReturn(cinema);
        when(hallMapper.mapHallListToHallDtoList(any())).thenReturn(expected);
        when(hallRepository.findAllByCinemaId(anyInt())).thenReturn(HallFactory.getDefaultHallList());

        final List<HallDto> result = hallService.getHallsByCinemaId(cinema.getId());

        assertEquals(expected, result);
    }

    /**
     * Verifies that get Hall By Id hall Found success.
     */
    @Test
    void testGetHallById_hallFound_success() {
        final Hall expected = HallFactory.getDefaultHall();
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Hall hall = hallService.getHallById(HallConstants.ID);

        assertEquals(expected, hall);
    }

    /**
     * Verifies that get Hall By Id hall Not Found throws Hall Not Found Exception.
     */
    @Test
    void testGetHallById_hallNotFound_throwsHallNotFoundException() {
        when(hallRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(HallNotFoundException.class, () -> hallService.getHallById(HallConstants.ID));
    }

    /**
     * Verifies that get Hall DTO By Id hall DTO Found success.
     */
    @Test
    void testGetHallDtoById_hallDtoFound_success() {
        final HallDto expected = HallFactory.getDefaultHallDto();
        when(hallMapper.mapHallToHallDto(any())).thenReturn(expected);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(new Hall()));

        final HallDto hall = hallService.getHallDtoById(HallConstants.ID);

        assertEquals(expected, hall);
    }

    /**
     * Verifies that update Hall hall Updated success.
     */
    @Test
    void testUpdateHall_hallUpdated_success() {
        final HallDto expected = HallFactory.getDefaultHallDto();
        when(hallMapper.mapHallToHallDto(any())).thenReturn(expected);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(HallFactory.getDefaultHall()));
        when(hallRepository.save(any())).thenReturn(HallFactory.getDefaultHall());

        final HallDto hall = hallService.updateHall(HallFactory.getDefaultHallRequest(), HallConstants.ID);

        assertEquals(expected, hall);
    }

    /**
     * Verifies that delete Hall hall Deleted success.
     */
    @Test
    void testDeleteHall_hallDeleted_success() {
        final HallDto expected = HallFactory.getDefaultHallDto();
        when(hallMapper.mapHallToHallDto(any())).thenReturn(expected);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(HallFactory.getDefaultHall()));

        final HallDto hall = hallService.deleteHall(HallConstants.ID);

        assertEquals(expected, hall);
    }
}





