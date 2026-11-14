package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.testutils.constants.ItemConstants;
import dev.vdrenkov.cineledger.testutils.factories.ItemFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @Test
    void testMapItemToItemDto_success() {
        final ItemDto itemDto = ItemMapper.mapItemToItemDto(ItemFactory.getDefaultItem());

        assertEquals(ItemConstants.ID, itemDto.id());
        assertEquals(ItemConstants.NAME, itemDto.name());
        assertEquals(ItemConstants.PRICE, itemDto.price(), 0.0);
        assertEquals(ItemConstants.QUANTITY, itemDto.quantity());
    }

    @Test
    void testMapItemToItemDtoList_success() {
        final List<ItemDto> itemDtos = ItemMapper.mapItemToItemDtoList(ItemFactory.getDefaultItemList());

        final ItemDto itemDto = itemDtos.getFirst();
        assertEquals(ItemConstants.ID, itemDto.id());
        assertEquals(ItemConstants.NAME, itemDto.name());
        assertEquals(ItemConstants.PRICE, itemDto.price(), 0.0);
        assertEquals(ItemConstants.QUANTITY, itemDto.quantity());
    }
}
