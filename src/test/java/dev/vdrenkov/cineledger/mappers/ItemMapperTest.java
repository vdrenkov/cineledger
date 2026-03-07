package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.testUtils.constants.ItemConstants;
import dev.vdrenkov.cineledger.testUtils.factories.ItemFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ItemMapperTest {

    @InjectMocks
    private ItemMapper itemMapper;

    @Test
    public void testMapItemToItemDto_success() {
        ItemDto itemDto = itemMapper.mapItemToItemDto(ItemFactory.getDefaultItem());

        Assertions.assertEquals(itemDto.getId(), ItemConstants.ID);
        Assertions.assertEquals(itemDto.getName(), ItemConstants.NAME);
        Assertions.assertEquals(itemDto.getPrice(), ItemConstants.PRICE, 0.0);
        Assertions.assertEquals(itemDto.getQuantity(), ItemConstants.QUANTITY);
    }

    @Test
    public void testMapItemToItemDtoList_success() {
        List<ItemDto> itemDtos = itemMapper.mapItemToItemDtoList(ItemFactory.getDefaultItemList());

        ItemDto itemDto = itemDtos.get(0);
        Assertions.assertEquals(itemDto.getId(), ItemConstants.ID);
        Assertions.assertEquals(itemDto.getName(), ItemConstants.NAME);
        Assertions.assertEquals(itemDto.getPrice(), ItemConstants.PRICE, 0.0);
        Assertions.assertEquals(itemDto.getQuantity(), ItemConstants.QUANTITY);
    }
}




