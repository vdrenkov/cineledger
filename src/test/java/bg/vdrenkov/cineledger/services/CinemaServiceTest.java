package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.CinemaAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.CinemaNotFoundException;
import bg.vdrenkov.cineledger.mappers.CinemaMapper;
import bg.vdrenkov.cineledger.models.dtos.CinemaDto;
import bg.vdrenkov.cineledger.models.entities.Cinema;
import bg.vdrenkov.cineledger.repositories.CinemaRepository;
import bg.vdrenkov.cineledger.testUtils.constants.CinemaConstants;
import bg.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CinemaServiceTest {

    @Mock
    private CinemaRepository cinemaRepository;

    @Mock
    private CinemaMapper cinemaMapper;

    @InjectMocks
    private CinemaService cinemaService;

    @Test
    public void testAddCinema_noExceptions_success() {
        Cinema expected = CinemaFactory.getDefaultCinema();
        when(cinemaRepository.save(any())).thenReturn(expected);

        Cinema cinema = cinemaService.addCinema(CinemaFactory.getDefaultCinemaRequest());

        assertEquals(expected, cinema);
    }

    @Test
    public void testAddCinema_cinemaExists_success() {
        assertThrows(CinemaAlreadyExistsException.class, () -> {

            when(cinemaRepository.findByCityAndAddress(anyString(), anyString())).thenReturn(Optional.of(new Cinema()));

            cinemaService.addCinema(CinemaFactory.getDefaultCinemaRequest());

        });
    }

    @Test
    public void testGetAllCinemas_cityAndAddressNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {

            cinemaService.getAllCinemas(null, null);

        });
    }

    @Test
    public void testGetAllCinemas_cityAndAddressNotNull_success() {
        List<CinemaDto> expected = CinemaFactory.getDefaultCinemaDtoList();
        when(cinemaMapper.mapCinemaToCinemaDtoList(any())).thenReturn(expected);
        when(cinemaRepository.findAllByCityAndAddress(anyString(), anyString())).thenReturn(
            CinemaFactory.getDefaultCinemaList());

        List<CinemaDto> result = cinemaService.getAllCinemas(CinemaConstants.CITY, CinemaConstants.ADDRESS);

        assertEquals(expected, result);
    }

    @Test
    public void testGetAllCinemas_cityNotNull_success() {
        List<CinemaDto> expected = CinemaFactory.getDefaultCinemaDtoList();
        when(cinemaMapper.mapCinemaToCinemaDtoList(any())).thenReturn(expected);
        when(cinemaRepository.findAllByCity(anyString())).thenReturn(CinemaFactory.getDefaultCinemaList());

        List<CinemaDto> result = cinemaService.getAllCinemas(CinemaConstants.CITY, null);

        assertEquals(expected, result);
    }

    @Test
    public void testGetAllCinemas_addressNotNull_success() {
        List<CinemaDto> expected = CinemaFactory.getDefaultCinemaDtoList();
        when(cinemaMapper.mapCinemaToCinemaDtoList(any())).thenReturn(expected);
        when(cinemaRepository.findAllByAddress(anyString())).thenReturn(CinemaFactory.getDefaultCinemaList());

        List<CinemaDto> result = cinemaService.getAllCinemas(null, CinemaConstants.ADDRESS);

        assertEquals(expected, result);
    }

    @Test
    public void testGetCinemaById_cinemaFound_success() {
        Cinema expected = CinemaFactory.getDefaultCinema();
        when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

        Cinema cinema = cinemaService.getCinemaById(CinemaConstants.ID);

        assertEquals(expected, cinema);
    }

    @Test
    public void testGetCinemaById_cinemaNotFound_throwsCinemaNotFoundException() {
        assertThrows(CinemaNotFoundException.class, () -> {

            cinemaService.getCinemaById(CinemaConstants.ID);

        });
    }

    @Test
    public void testGetCinemaDtoById_cinemaDtoFound_success() {
        CinemaDto expected = CinemaFactory.getDefaultCinemaDto();
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(expected);
        when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(new Cinema()));

        CinemaDto cinemaDto = cinemaService.getCinemaDtoById(CinemaConstants.ID);

        assertEquals(expected, cinemaDto);
    }

    @Test
    public void testUpdateCinema_cinemaUpdated_success() {
        CinemaDto expected = CinemaFactory.getDefaultCinemaDto();
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(expected);
        when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));
        when(cinemaRepository.save(any())).thenReturn(CinemaFactory.getDefaultCinema());

        CinemaDto cinemaDto = cinemaService.updateCinema(CinemaFactory.getDefaultCinemaRequest(), CinemaConstants.ID);

        assertEquals(expected, cinemaDto);
    }

    @Test
    public void testDeleteCinema_cinemaDeleted_success() {
        CinemaDto expected = CinemaFactory.getDefaultCinemaDto();
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(expected);
        when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

        CinemaDto cinemaDto = cinemaService.deleteCinema(CinemaConstants.ID);

        assertEquals(expected, cinemaDto);
    }

    @Test
    public void testUpdateCinemaAverageRating_success() {
        double newRating = 4.5;

        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());
        when(cinemaRepository.findById(anyInt())).thenReturn(Optional.of(CinemaFactory.getDefaultCinema()));

        CinemaDto result = cinemaService.updateCinemaAverageRating(newRating, CinemaConstants.ID);

        assertEquals(newRating, result.getAverageRating(), 0.01);
    }
}



