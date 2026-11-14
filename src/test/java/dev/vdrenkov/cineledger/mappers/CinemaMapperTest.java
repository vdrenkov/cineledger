package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.testutils.constants.CinemaConstants;
import dev.vdrenkov.cineledger.testutils.factories.CinemaFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CinemaMapperTest {

    @Test
    void testMapCinemaToCinemaDto_success() {
        final CinemaDto cinemaDto = CinemaMapper.mapCinemaToCinemaDto(CinemaFactory.getDefaultCinema());

        assertEquals(CinemaConstants.ID, cinemaDto.id());
        assertEquals(CinemaConstants.ADDRESS, cinemaDto.address());
        assertEquals(CinemaConstants.CITY, cinemaDto.city());
        assertEquals(CinemaConstants.AVERAGE_RATING, cinemaDto.averageRating(), 0.0);
    }

    @Test
    void testMapCinemaToCinemaDtoList() {
        final List<CinemaDto> cinemaDtos = CinemaMapper.mapCinemaToCinemaDtoList(CinemaFactory.getDefaultCinemaList());

        final CinemaDto cinemaDto = cinemaDtos.getFirst();
        assertEquals(CinemaConstants.ID, cinemaDto.id());
        assertEquals(CinemaConstants.ADDRESS, cinemaDto.address());
        assertEquals(CinemaConstants.CITY, cinemaDto.city());
        assertEquals(CinemaConstants.AVERAGE_RATING, cinemaDto.averageRating(), 0.0);
    }
}
