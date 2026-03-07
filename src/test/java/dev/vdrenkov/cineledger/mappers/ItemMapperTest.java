package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.testutil.constants.ItemConstants;
import dev.vdrenkov.cineledger.testutil.factories.ItemFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Tests item mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @InjectMocks
    private ItemMapper itemMapper;

    /**
     * Verifies that map Item To Item DTO success.
     */
    @Test
    void testMapItemToItemDto_success() {
        final ItemDto itemDto = itemMapper.mapItemToItemDto(ItemFactory.getDefaultItem());

        Assertions.assertEquals(itemDto.getId(), ItemConstants.ID);
        Assertions.assertEquals(itemDto.getName(), ItemConstants.NAME);
        Assertions.assertEquals(itemDto.getPrice(), ItemConstants.PRICE, 0.0);
        Assertions.assertEquals(itemDto.getQuantity(), ItemConstants.QUANTITY);
    }

    /**
     * Verifies that map Item To Item DTO List success.
     */
    @Test
    void testMapItemToItemDtoList_success() {
        final List<ItemDto> itemDtos = itemMapper.mapItemToItemDtoList(ItemFactory.getDefaultItemList());

        final ItemDto itemDto = itemDtos.get(0);
        Assertions.assertEquals(itemDto.getId(), ItemConstants.ID);
        Assertions.assertEquals(itemDto.getName(), ItemConstants.NAME);
        Assertions.assertEquals(itemDto.getPrice(), ItemConstants.PRICE, 0.0);
        Assertions.assertEquals(itemDto.getQuantity(), ItemConstants.QUANTITY);
    }
}




