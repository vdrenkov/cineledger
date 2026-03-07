package bg.vdrenkov.cineledger.mappers;

import bg.vdrenkov.cineledger.models.dtos.HallDto;
import bg.vdrenkov.cineledger.testUtils.constants.HallConstants;
import bg.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
import bg.vdrenkov.cineledger.testUtils.factories.HallFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HallMapperTest {

    @Mock
    private CinemaMapper cinemaMapper;

    @InjectMocks
    private HallMapper hallMapper;

    @Test
    public void testMapHallToHallDto_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        HallDto hallDto = hallMapper.mapHallToHallDto(HallFactory.getDefaultHall());

        Assertions.assertEquals(HallConstants.ID, hallDto.getId());
        Assertions.assertEquals(HallConstants.CAPACITY, hallDto.getCapacity());
        assertNotNull(hallDto.getCinema());
    }

    @Test
    public void testMapHallListToHallDtoList_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        List<HallDto> hallDtoList = hallMapper.mapHallListToHallDtoList(HallFactory.getDefaultHallList());
        HallDto hallDto = hallDtoList.get(0);

        Assertions.assertEquals(HallConstants.ID, hallDto.getId());
        Assertions.assertEquals(HallConstants.CAPACITY, hallDto.getCapacity());
        assertNotNull(hallDto.getCinema());
    }
}



