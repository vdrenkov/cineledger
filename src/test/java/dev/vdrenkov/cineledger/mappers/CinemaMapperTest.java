package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.testutil.constants.CinemaConstants;
import dev.vdrenkov.cineledger.testutil.factories.CinemaFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Tests cinema mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class CinemaMapperTest {

    @InjectMocks
    private CinemaMapper cinemaMapper;

    /**
     * Verifies that map Cinema To Cinema DTO success.
     */
    @Test
    void testMapCinemaToCinemaDto_success() {
        final CinemaDto cinemaDto = cinemaMapper.mapCinemaToCinemaDto(CinemaFactory.getDefaultCinema());

        Assertions.assertEquals(cinemaDto.getId(), CinemaConstants.ID);
        Assertions.assertEquals(cinemaDto.getAddress(), CinemaConstants.ADDRESS);
        Assertions.assertEquals(cinemaDto.getCity(), CinemaConstants.CITY);
        Assertions.assertEquals(cinemaDto.getAverageRating(), CinemaConstants.AVERAGE_RATING, 0.0);
    }

    /**
     * Verifies that map Cinema To Cinema DTO List.
     */
    @Test
    void testMapCinemaToCinemaDtoList() {
        final List<CinemaDto> cinemaDtos = cinemaMapper.mapCinemaToCinemaDtoList(CinemaFactory.getDefaultCinemaList());

        final CinemaDto cinemaDto = cinemaDtos.get(0);
        Assertions.assertEquals(cinemaDto.getId(), CinemaConstants.ID);
        Assertions.assertEquals(cinemaDto.getAddress(), CinemaConstants.ADDRESS);
        Assertions.assertEquals(cinemaDto.getCity(), CinemaConstants.CITY);
        Assertions.assertEquals(cinemaDto.getAverageRating(), CinemaConstants.AVERAGE_RATING, 0.0);
    }
}



