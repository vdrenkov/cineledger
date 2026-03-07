package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.ItemAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.ItemNotFoundException;
import dev.vdrenkov.cineledger.exceptions.NoAvailableItemsException;
import dev.vdrenkov.cineledger.mappers.ItemMapper;
import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.repositories.ItemRepository;
import dev.vdrenkov.cineledger.testUtils.constants.ItemConstants;
import dev.vdrenkov.cineledger.testUtils.factories.ItemFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testGetAllItems_noExceptions_success() {
        List<ItemDto> expected = ItemFactory.getDefaultItemDtoList();
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(expected);
        when(itemRepository.findAll()).thenReturn(ItemFactory.getDefaultItemList());

        List<ItemDto> actual = itemService.getAllItems();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetItemDtoById_itemFound_success() {
        ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));

        ItemDto item = itemService.getItemDtoById(ItemConstants.ID);

        assertEquals(expected, item);
    }

    @Test
    public void testGetItemDtoById_itemNotFound_throwsItemNotFoundException() {
        assertThrows(ItemNotFoundException.class, () -> {

            when(itemRepository.findById(anyInt())).thenThrow(new ItemNotFoundException(anyString()));
            itemService.getItemDtoById(ItemConstants.ID);

        });
    }

    @Test
    public void testFindItemById_itemFound_success() {
        Item expected = ItemFactory.getDefaultItem();
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Item item = itemService.getItemById(ItemConstants.ID);

        assertEquals(expected, item);
    }

    @Test
    public void testFindItemById_itemNotFound_throwsItemNotFoundException() {
        assertThrows(ItemNotFoundException.class, () -> {

            when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
            itemService.getItemById(ItemConstants.ID);

        });
    }

    @Test
    public void testGetItemDtoByName_noExceptions_success() {
        ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findByName(anyString())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));

        ItemDto itemDto = itemService.getItemDtoByName(ItemConstants.NAME);

        assertEquals(expected, itemDto);
    }

    @Test
    public void testGetItemDtoByName_throwsCategoryNotFoundException() {
        assertThrows(ItemNotFoundException.class, () -> {

            when(itemRepository.findByName(anyString())).thenReturn(Optional.empty());

            itemService.getItemDtoByName(ItemConstants.NAME);

        });
    }

    @Test
    public void testAddItem_noExceptions_success() {
        Item expected = ItemFactory.getDefaultItem();
        when(itemRepository.save(any())).thenReturn(expected);

        Item item = itemService.addItem(ItemFactory.getDefaultItemRequest());

        assertEquals(expected, item);
    }

    @Test
    public void testAddItem_itemAlreadyExists_throwsExistingItemException() {
        assertThrows(ItemAlreadyExistsException.class, () -> {

            when(itemRepository.findByName(anyString())).thenReturn(Optional.of(new Item()));

            itemService.addItem(ItemFactory.getDefaultItemRequest());

        });
    }

    @Test
    public void testUpdateItem_itemUpdated_success() {
        ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));
        when(itemRepository.save(any())).thenReturn(ItemFactory.getDefaultItem());

        ItemDto itemDto = itemService.editItem(ItemFactory.getDefaultItemRequest(), ItemConstants.ID);

        assertEquals(expected, itemDto);
    }

    @Test
    public void testDeleteItem_itemDeleted_success() {
        ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));

        ItemDto itemDto = itemService.removeItem(ItemConstants.ID);

        assertEquals(expected, itemDto);
    }

    @Test
    public void testIncrementItemQuantity_shouldIncrementedItemQuantity_success() {
        Item item = new Item();
        item.setQuantity(5);

        int incrementedQuantity = itemService.incrementItemQuantity(item);

        assertEquals(6, incrementedQuantity);
        assertEquals(6, item.getQuantity());
    }

    @Test
    public void testDecrementItemQuantity_shouldDecrementItemQuantity_success() {
        Item item = new Item();
        item.setQuantity(5);

        int decrementedQuantity = itemService.decrementItemQuantity(item);

        assertEquals(4, decrementedQuantity);
        assertEquals(4, item.getQuantity());
    }

    @Test
    public void testDecrementItemQuantity_shouldThrowNoAvailableItemsException() {
        assertThrows(NoAvailableItemsException.class, () -> {

            Item item = new Item();
            item.setQuantity(0);

            itemService.decrementItemQuantity(item);

        });
    }

    @Test
    public void testGetItemsByQuantity_noExceptions_isBelowFalse_success() {
        List<ItemDto> expected = ItemFactory.getDefaultItemDtoList();
        when(itemRepository.findAll()).thenReturn(ItemFactory.getDefaultItemList());
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(expected);

        List<ItemDto> resultList = itemService.getItemsByQuantity(ItemConstants.QUANTITY - 1, ItemConstants.IS_BELLOW);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetItemsByQuantity_noExceptions_isBelowTrue_success() {
        List<ItemDto> expected = ItemFactory.getDefaultItemDtoList();
        when(itemRepository.findAll()).thenReturn(ItemFactory.getDefaultItemList());
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(expected);

        List<ItemDto> resultList = itemService.getItemsByQuantity(ItemConstants.QUANTITY + 1, true);

        assertEquals(expected, resultList);
    }
}




