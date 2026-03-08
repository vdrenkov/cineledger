package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.testutils.constants.HallConstants;
import dev.vdrenkov.cineledger.testutils.factories.HallFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests hall mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class HallMapperTest {
    /**
     * Verifies that map Hall To Hall DTO success.
     */
    @Test
    void testMapHallToHallDto_success() {
        final HallDto hallDto = HallMapper.mapHallToHallDto(HallFactory.getDefaultHall());

        assertEquals(HallConstants.ID, hallDto.getId());
        assertEquals(HallConstants.CAPACITY, hallDto.getCapacity());
        assertNotNull(hallDto.getCinema());
    }

    /**
     * Verifies that map Hall List To Hall DTO List success.
     */
    @Test
    void testMapHallListToHallDtoList_success() {
        final List<HallDto> hallDtoList = HallMapper.mapHallListToHallDtoList(HallFactory.getDefaultHallList());
        final HallDto hallDto = hallDtoList.getFirst();

        assertEquals(HallConstants.ID, hallDto.getId());
        assertEquals(HallConstants.CAPACITY, hallDto.getCapacity());
        assertNotNull(hallDto.getCinema());
    }
}



