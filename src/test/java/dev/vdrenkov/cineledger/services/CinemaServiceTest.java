package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dev.vdrenkov.cineledger.exceptions.CinemaAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CinemaNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.requests.CinemaRequest;
import dev.vdrenkov.cineledger.repositories.CinemaRepository;
import dev.vdrenkov.cineledger.testutils.constants.CinemaConstants;
import dev.vdrenkov.cineledger.testutils.factories.CinemaFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {
    final CinemaRequest cinemaRequest = CinemaFactory.getDefaultCinemaRequest();

    @Mock private CinemaRepository cinemaRepository;

    @InjectMocks private CinemaService cinemaService;

    @Test
    void testAddCinema_noExceptions_success() {
        final Cinema expected = CinemaFactory.getDefaultCinema();
        when(cinemaRepository.save(any())).thenReturn(expected);

        final Cinema cinema = cinemaService.addCinema(cinemaRequest);

        assertEquals(expected, cinema);
    }

    @Test
    void testAddCinema_cinemaExists_success() {
        when(cinemaRepository.findByCityAndAddress(anyString(), anyString()))
                .thenReturn(Optional.of(new Cinema()));

        assertThrows(
                CinemaAlreadyExistsException.class, () -> cinemaService.addCinema(cinemaRequest));
    }

    @Test
    void testGetAllCinemas_cityAndAddressNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> cinemaService.getAllCinemas(null, null));
    }

    @Test
    void testGetAllCinemas_cityAndAddressNotNull_success() {
        final List<CinemaDto> expected = CinemaFactory.getDefaultCinemaDtoList();
        when(cinemaRepository.findAllByCityAndAddress(anyString(), anyString()))
                .thenReturn(CinemaFactory.getDefaultCinemaList());

        final List<CinemaDto> result =
                cinemaService.getAllCinemas(CinemaConstants.CITY, CinemaConstants.ADDRESS);

        assertEquals(expected, result);
    }

    @Test
    void testGetAllCinemas_cityNotNull_success() {
        final List<CinemaDto> expected = CinemaFactory.getDefaultCinemaDtoList();
        when(cinemaRepository.findAllByCity(anyString()))
                .thenReturn(CinemaFactory.getDefaultCinemaList());

        final List<CinemaDto> result = cinemaService.getAllCinemas(CinemaConstants.CITY, null);

        assertEquals(expected, result);
    }

    @Test
    void testGetAllCinemas_addressNotNull_success() {
        final List<CinemaDto> expected = CinemaFactory.getDefaultCinemaDtoList();
        when(cinemaRepository.findAllByAddress(anyString()))
                .thenReturn(CinemaFactory.getDefaultCinemaList());

        final List<CinemaDto> result = cinemaService.getAllCinemas(null, CinemaConstants.ADDRESS);

        assertEquals(expected, result);
    }

    @Test
    void testGetCinemaById_cinemaFound_success() {
        final Cinema expected = CinemaFactory.getDefaultCinema();
        when(cinemaRepository.findById(anyInt()))
                .thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

        final Cinema cinema = cinemaService.getCinemaById(CinemaConstants.ID);

        assertEquals(expected, cinema);
    }

    @Test
    void testGetCinemaById_cinemaNotFound_throwsCinemaNotFoundException() {
        assertThrows(
                CinemaNotFoundException.class,
                () -> cinemaService.getCinemaById(CinemaConstants.ID));
    }

    @Test
    void testGetCinemaDtoById_cinemaDtoFound_success() {
        final CinemaDto expected = CinemaFactory.getDefaultCinemaDto();
        when(cinemaRepository.findById(anyInt()))
                .thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

        final CinemaDto cinemaDto = cinemaService.getCinemaDtoById(CinemaConstants.ID);

        assertEquals(expected, cinemaDto);
    }

    @Test
    void testUpdateCinema_cinemaUpdated_success() {
        final CinemaDto expected = CinemaFactory.getDefaultCinemaDto();
        when(cinemaRepository.findByIdForUpdate(anyInt()))
                .thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

        final CinemaDto cinemaDto = cinemaService.updateCinema(cinemaRequest, CinemaConstants.ID);

        assertEquals(expected, cinemaDto);
    }

    @Test
    void testDeleteCinema_cinemaDeleted_success() {
        final CinemaDto expected = CinemaFactory.getDefaultCinemaDto();
        when(cinemaRepository.findById(anyInt()))
                .thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

        final CinemaDto cinemaDto = cinemaService.deleteCinema(CinemaConstants.ID);

        assertEquals(expected, cinemaDto);
    }

    @Test
    void testUpdateCinemaAverageRating_success() {
        final double newRating = 4.5;

        when(cinemaRepository.findById(anyInt()))
                .thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

        final CinemaDto result =
                cinemaService.updateCinemaAverageRating(newRating, CinemaConstants.ID);

        assertEquals(newRating, result.averageRating(), 0.01);
    }
}
