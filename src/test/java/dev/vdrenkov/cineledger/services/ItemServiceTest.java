package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.ItemAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.ItemNotFoundException;
import dev.vdrenkov.cineledger.exceptions.NoAvailableItemsException;
import dev.vdrenkov.cineledger.mappers.ItemMapper;
import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.requests.ItemRequest;
import dev.vdrenkov.cineledger.repositories.ItemRepository;
import dev.vdrenkov.cineledger.testutil.constants.ItemConstants;
import dev.vdrenkov.cineledger.testutil.factories.ItemFactory;
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

/**
 * Tests item service behavior.
 */
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    final ItemRequest itemRequest = ItemFactory.getDefaultItemRequest();

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    /**
     * Verifies that get All Items no Exceptions success.
     */
    @Test
    void testGetAllItems_noExceptions_success() {
        final List<ItemDto> expected = ItemFactory.getDefaultItemDtoList();
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(expected);
        when(itemRepository.findAll()).thenReturn(ItemFactory.getDefaultItemList());

        final List<ItemDto> actual = itemService.getAllItems();

        assertEquals(expected, actual);
    }

    /**
     * Verifies that get Item DTO By Id item Found success.
     */
    @Test
    void testGetItemDtoById_itemFound_success() {
        final ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));

        final ItemDto item = itemService.getItemDtoById(ItemConstants.ID);

        assertEquals(expected, item);
    }

    /**
     * Verifies that get Item DTO By Id item Not Found throws Item Not Found Exception.
     */
    @Test
    void testGetItemDtoById_itemNotFound_throwsItemNotFoundException() {
        when(itemRepository.findById(anyInt())).thenThrow(new ItemNotFoundException(anyString()));
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemDtoById(ItemConstants.ID));
    }

    /**
     * Verifies that find Item By Id item Found success.
     */
    @Test
    void testFindItemById_itemFound_success() {
        final Item expected = ItemFactory.getDefaultItem();
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        final Item item = itemService.getItemById(ItemConstants.ID);

        assertEquals(expected, item);
    }

    /**
     * Verifies that find Item By Id item Not Found throws Item Not Found Exception.
     */
    @Test
    void testFindItemById_itemNotFound_throwsItemNotFoundException() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(ItemConstants.ID));
    }

    /**
     * Verifies that get Item DTO By Name no Exceptions success.
     */
    @Test
    void testGetItemDtoByName_noExceptions_success() {
        final ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findByName(anyString())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));

        final ItemDto itemDto = itemService.getItemDtoByName(ItemConstants.NAME);

        assertEquals(expected, itemDto);
    }

    /**
     * Verifies that get Item DTO By Name throws Category Not Found Exception.
     */
    @Test
    void testGetItemDtoByName_throwsCategoryNotFoundException() {
        when(itemRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemDtoByName(ItemConstants.NAME));
    }

    /**
     * Verifies that add Item no Exceptions success.
     */
    @Test
    void testAddItem_noExceptions_success() {
        final Item expected = ItemFactory.getDefaultItem();
        when(itemRepository.save(any())).thenReturn(expected);

        final Item item = itemService.addItem(itemRequest);

        assertEquals(expected, item);
    }

    /**
     * Verifies that add Item item Already Exists throws Existing Item Exception.
     */
    @Test
    void testAddItem_itemAlreadyExists_throwsExistingItemException() {
        when(itemRepository.findByName(anyString())).thenReturn(Optional.of(new Item()));

        assertThrows(ItemAlreadyExistsException.class, () -> itemService.addItem(itemRequest));
    }

    /**
     * Verifies that update Item item Updated success.
     */
    @Test
    void testUpdateItem_itemUpdated_success() {
        final ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));
        when(itemRepository.save(any())).thenReturn(ItemFactory.getDefaultItem());

        final ItemDto itemDto = itemService.editItem(itemRequest, ItemConstants.ID);

        assertEquals(expected, itemDto);
    }

    /**
     * Verifies that delete Item item Deleted success.
     */
    @Test
    void testDeleteItem_itemDeleted_success() {
        final ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));

        final ItemDto itemDto = itemService.removeItem(ItemConstants.ID);

        assertEquals(expected, itemDto);
    }

    /**
     * Verifies that increment Item Quantity should Incremented Item Quantity success.
     */
    @Test
    void testIncrementItemQuantity_shouldIncrementedItemQuantity_success() {
        final Item item = new Item();
        item.setQuantity(5);

        final int incrementedQuantity = itemService.incrementItemQuantity(item);

        assertEquals(6, incrementedQuantity);
        assertEquals(6, item.getQuantity());
    }

    /**
     * Verifies that decrement Item Quantity should Decrement Item Quantity success.
     */
    @Test
    void testDecrementItemQuantity_shouldDecrementItemQuantity_success() {
        final Item item = new Item();
        item.setQuantity(5);

        final int decrementedQuantity = itemService.decrementItemQuantity(item);

        assertEquals(4, decrementedQuantity);
        assertEquals(4, item.getQuantity());
    }

    /**
     * Verifies that decrement Item Quantity should Throw No Available Items Exception.
     */
    @Test
    void testDecrementItemQuantity_shouldThrowNoAvailableItemsException() {
        final Item item = new Item();
        item.setQuantity(0);

        assertThrows(NoAvailableItemsException.class, () -> itemService.decrementItemQuantity(item));
    }

    /**
     * Verifies that get Items By Quantity no Exceptions is Below False success.
     */
    @Test
    void testGetItemsByQuantity_noExceptions_isBelowFalse_success() {
        final List<ItemDto> expected = ItemFactory.getDefaultItemDtoList();
        when(itemRepository.findAll()).thenReturn(ItemFactory.getDefaultItemList());
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(expected);

        final List<ItemDto> resultList = itemService.getItemsByQuantity(ItemConstants.QUANTITY - 1,
            ItemConstants.IS_BELLOW);

        assertEquals(expected, resultList);
    }

    /**
     * Verifies that get Items By Quantity no Exceptions is Below True success.
     */
    @Test
    void testGetItemsByQuantity_noExceptions_isBelowTrue_success() {
        final List<ItemDto> expected = ItemFactory.getDefaultItemDtoList();
        when(itemRepository.findAll()).thenReturn(ItemFactory.getDefaultItemList());
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(expected);

        final List<ItemDto> resultList = itemService.getItemsByQuantity(ItemConstants.QUANTITY + 1, true);

        assertEquals(expected, resultList);
    }
}




