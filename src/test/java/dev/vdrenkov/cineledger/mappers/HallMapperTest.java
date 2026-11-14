package dev.vdrenkov.cineledger.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.testutils.constants.HallConstants;
import dev.vdrenkov.cineledger.testutils.factories.HallFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class HallMapperTest {

    @Test
    void testMapHallToHallDto_success() {
        final HallDto hallDto = HallMapper.mapHallToHallDto(HallFactory.getDefaultHall());

        assertEquals(HallConstants.ID, hallDto.id());
        assertEquals(HallConstants.CAPACITY, hallDto.capacity());
        assertNotNull(hallDto.cinema());
    }

    @Test
    void testMapHallListToHallDtoList_success() {
        final List<HallDto> hallDtoList =
                HallMapper.mapHallListToHallDtoList(HallFactory.getDefaultHallList());
        final HallDto hallDto = hallDtoList.getFirst();

        assertEquals(HallConstants.ID, hallDto.id());
        assertEquals(HallConstants.CAPACITY, hallDto.capacity());
        assertNotNull(hallDto.cinema());
    }
}
