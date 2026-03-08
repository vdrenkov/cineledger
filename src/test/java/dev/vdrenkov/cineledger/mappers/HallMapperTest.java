package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.testutil.constants.HallConstants;
import dev.vdrenkov.cineledger.testutil.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutil.factories.HallFactory;
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

/**
 * Tests hall mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class HallMapperTest {

    @Mock
    private CinemaMapper cinemaMapper;

    @InjectMocks
    private HallMapper hallMapper;

    /**
     * Verifies that map Hall To Hall DTO success.
     */
    @Test
    void testMapHallToHallDto_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        final HallDto hallDto = hallMapper.mapHallToHallDto(HallFactory.getDefaultHall());

        Assertions.assertEquals(HallConstants.ID, hallDto.getId());
        Assertions.assertEquals(HallConstants.CAPACITY, hallDto.getCapacity());
        assertNotNull(hallDto.getCinema());
    }

    /**
     * Verifies that map Hall List To Hall DTO List success.
     */
    @Test
    void testMapHallListToHallDtoList_success() {
        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());

        final List<HallDto> hallDtoList = hallMapper.mapHallListToHallDtoList(HallFactory.getDefaultHallList());
        final HallDto hallDto = hallDtoList.getFirst();

        Assertions.assertEquals(HallConstants.ID, hallDto.getId());
        Assertions.assertEquals(HallConstants.CAPACITY, hallDto.getCapacity());
        assertNotNull(hallDto.getCinema());
    }
}



