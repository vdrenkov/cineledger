package bg.vdrenkov.cineledger.mappers;

import org.junit.jupiter.api.extension.ExtendWith;
import bg.vdrenkov.cineledger.models.dtos.ItemDto;
import bg.vdrenkov.cineledger.testUtils.constants.ItemConstants;
import bg.vdrenkov.cineledger.testUtils.factories.ItemFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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




