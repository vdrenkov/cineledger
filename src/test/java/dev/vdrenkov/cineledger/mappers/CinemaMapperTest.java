package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.testutils.constants.CinemaConstants;
import dev.vdrenkov.cineledger.testutils.factories.CinemaFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests cinema mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class CinemaMapperTest {
    /**
     * Verifies that map Cinema To Cinema DTO success.
     */
    @Test
    void testMapCinemaToCinemaDto_success() {
        final CinemaDto cinemaDto = CinemaMapper.mapCinemaToCinemaDto(CinemaFactory.getDefaultCinema());

        assertEquals(CinemaConstants.ID, cinemaDto.getId());
        assertEquals(CinemaConstants.ADDRESS, cinemaDto.getAddress());
        assertEquals(CinemaConstants.CITY, cinemaDto.getCity());
        assertEquals(CinemaConstants.AVERAGE_RATING, cinemaDto.getAverageRating(), 0.0);
    }

    /**
     * Verifies that map Cinema To Cinema DTO List.
     */
    @Test
    void testMapCinemaToCinemaDtoList() {
        final List<CinemaDto> cinemaDtos = CinemaMapper.mapCinemaToCinemaDtoList(CinemaFactory.getDefaultCinemaList());

        final CinemaDto cinemaDto = cinemaDtos.getFirst();
        assertEquals(CinemaConstants.ID, cinemaDto.getId());
        assertEquals(CinemaConstants.ADDRESS, cinemaDto.getAddress());
        assertEquals(CinemaConstants.CITY, cinemaDto.getCity());
        assertEquals(CinemaConstants.AVERAGE_RATING, cinemaDto.getAverageRating(), 0.0);
    }
}



