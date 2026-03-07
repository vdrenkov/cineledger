package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.testUtils.constants.CinemaConstants;
import dev.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CinemaMapperTest {

    @InjectMocks
    private CinemaMapper cinemaMapper;

    @Test
    public void testMapCinemaToCinemaDto_success() {
        CinemaDto cinemaDto = cinemaMapper.mapCinemaToCinemaDto(CinemaFactory.getDefaultCinema());

        Assertions.assertEquals(cinemaDto.getId(), CinemaConstants.ID);
        Assertions.assertEquals(cinemaDto.getAddress(), CinemaConstants.ADDRESS);
        Assertions.assertEquals(cinemaDto.getCity(), CinemaConstants.CITY);
        Assertions.assertEquals(cinemaDto.getAverageRating(), CinemaConstants.AVERAGE_RATING, 0.0);
    }

    @Test
    public void testMapCinemaToCinemaDtoList() {
        List<CinemaDto> cinemaDtos = cinemaMapper.mapCinemaToCinemaDtoList(CinemaFactory.getDefaultCinemaList());

        CinemaDto cinemaDto = cinemaDtos.get(0);
        Assertions.assertEquals(cinemaDto.getId(), CinemaConstants.ID);
        Assertions.assertEquals(cinemaDto.getAddress(), CinemaConstants.ADDRESS);
        Assertions.assertEquals(cinemaDto.getCity(), CinemaConstants.CITY);
        Assertions.assertEquals(cinemaDto.getAverageRating(), CinemaConstants.AVERAGE_RATING, 0.0);
    }
}



