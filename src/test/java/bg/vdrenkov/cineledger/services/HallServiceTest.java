package bg.vdrenkov.cineledger.services;

import org.junit.jupiter.api.extension.ExtendWith;
import bg.vdrenkov.cineledger.exceptions.HallNotFoundException;
import bg.vdrenkov.cineledger.mappers.HallMapper;
import bg.vdrenkov.cineledger.models.entities.Cinema;
import bg.vdrenkov.cineledger.models.entities.Hall;
import bg.vdrenkov.cineledger.repositories.HallRepository;
import bg.vdrenkov.cineledger.testUtils.constants.HallConstants;
import bg.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
import bg.vdrenkov.cineledger.testUtils.factories.HallFactory;
import bg.vdrenkov.cineledger.models.dtos.HallDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
public class HallServiceTest {

    @Mock
    private HallRepository hallRepository;

    @Mock
    private HallMapper hallMapper;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private HallService hallService;

    @Test
    public void testAddHall_noExceptions_success() {
        Hall expected = HallFactory.getDefaultHall();
        when(hallRepository.save(any())).thenReturn(expected);
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());

        Hall hall = hallService.addHall(HallFactory.getDefaultHallRequest());

        assertEquals(expected, hall);
    }

    @Test
    public void testGetHallByCinemaId_cinemaFound_success() {
        Cinema cinema = CinemaFactory.getDefaultCinema();
        List<HallDto> expected = HallFactory.getDefaultHallDtoList();

        when(cinemaService.getCinemaById(anyInt())).thenReturn(cinema);
        when(hallMapper.mapHallListToHallDtoList(any())).thenReturn(expected);
        when(hallRepository.findAllByCinemaId(anyInt())).thenReturn(HallFactory.getDefaultHallList());

        List<HallDto> result = hallService.getHallsByCinemaId(cinema.getId());

        assertEquals(expected, result);
    }

    @Test
    public void testGetHallById_hallFound_success() {
        Hall expected = HallFactory.getDefaultHall();
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Hall hall = hallService.getHallById(HallConstants.ID);

        assertEquals(expected, hall);
    }

    @Test
    public void testGetHallById_hallNotFound_throwsHallNotFoundException() {
      assertThrows(HallNotFoundException.class, () -> {

          when(hallRepository.findById(anyInt())).thenReturn(Optional.empty());
          hallService.getHallById(HallConstants.ID);
      
      });
    }

    @Test
    public void testGetHallDtoById_hallDtoFound_success() {
        HallDto expected = HallFactory.getDefaultHallDto();
        when(hallMapper.mapHallToHallDto(any())).thenReturn(expected);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(new Hall()));

        HallDto hall = hallService.getHallDtoById(HallConstants.ID);

        assertEquals(expected, hall);
    }

    @Test
    public void testUpdateHall_hallUpdated_success() {
        HallDto expected = HallFactory.getDefaultHallDto();
        when(hallMapper.mapHallToHallDto(any())).thenReturn(expected);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(HallFactory.getDefaultHall()));
        when(hallRepository.save(any())).thenReturn(HallFactory.getDefaultHall());

        HallDto hall = hallService.updateHall(HallFactory.getDefaultHallRequest(), HallConstants.ID);

        assertEquals(expected, hall);
    }

    @Test
    public void testDeleteHall_hallDeleted_success() {
        HallDto expected = HallFactory.getDefaultHallDto();
        when(hallMapper.mapHallToHallDto(any())).thenReturn(expected);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(HallFactory.getDefaultHall()));

        HallDto hall = hallService.deleteHall(HallConstants.ID);

        assertEquals(expected, hall);
    }
}





