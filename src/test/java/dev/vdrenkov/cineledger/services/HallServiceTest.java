package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import dev.vdrenkov.cineledger.exceptions.HallNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.repositories.HallRepository;
import dev.vdrenkov.cineledger.repositories.ProjectionRepository;
import dev.vdrenkov.cineledger.testutils.constants.HallConstants;
import dev.vdrenkov.cineledger.testutils.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutils.factories.HallFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class HallServiceTest {

    @Mock private HallRepository hallRepository;

    @Mock private CinemaService cinemaService;

    @Mock private ProjectionRepository projectionRepository;

    @InjectMocks private HallService hallService;

    @Test
    void testAddHall_noExceptions_success() {
        final Hall expected = HallFactory.getDefaultHall();
        when(hallRepository.save(any())).thenReturn(expected);
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());

        final Hall hall = hallService.addHall(HallFactory.getDefaultHallRequest());

        assertEquals(expected, hall);
    }

    @Test
    void testGetHallByCinemaId_cinemaFound_success() {
        final Cinema cinema = CinemaFactory.getDefaultCinema();
        final List<HallDto> expected = HallFactory.getDefaultHallDtoList();

        when(cinemaService.getCinemaById(anyInt())).thenReturn(cinema);
        when(hallRepository.findAllByCinemaId(anyInt()))
                .thenReturn(HallFactory.getDefaultHallList());

        final List<HallDto> result = hallService.getHallsByCinemaId(cinema.getId());

        assertEquals(expected, result);
    }

    @Test
    void testGetHallById_hallFound_success() {
        final Hall expected = HallFactory.getDefaultHall();
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Hall hall = hallService.getHallById(HallConstants.ID);

        assertEquals(expected, hall);
    }

    @Test
    void testGetHallById_hallNotFound_throwsHallNotFoundException() {
        when(hallRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(HallNotFoundException.class, () -> hallService.getHallById(HallConstants.ID));
    }

    @Test
    void testGetHallDtoById_hallDtoFound_success() {
        final HallDto expected = HallFactory.getDefaultHallDto();
        when(hallRepository.findById(anyInt()))
                .thenReturn(Optional.of(HallFactory.getDefaultHall()));

        final HallDto hall = hallService.getHallDtoById(HallConstants.ID);

        assertEquals(expected, hall);
    }

    @Test
    void testUpdateHall_hallUpdated_success() {
        final HallDto expected = HallFactory.getDefaultHallDto();
        when(hallRepository.findByIdForUpdate(anyInt()))
                .thenReturn(Optional.of(HallFactory.getDefaultHall()));
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());
        when(hallRepository.save(any())).thenReturn(HallFactory.getDefaultHall());

        final HallDto hall =
                hallService.updateHall(HallFactory.getDefaultHallRequest(), HallConstants.ID);

        assertEquals(expected, hall);
    }

    @Test
    void testDeleteHall_hallDeleted_success() {
        final HallDto expected = HallFactory.getDefaultHallDto();
        when(hallRepository.findById(anyInt()))
                .thenReturn(Optional.of(HallFactory.getDefaultHall()));

        final HallDto hall = hallService.deleteHall(HallConstants.ID);

        assertEquals(expected, hall);
    }
}
