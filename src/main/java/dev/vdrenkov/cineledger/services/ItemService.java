package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.ItemAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.ItemNotFoundException;
import dev.vdrenkov.cineledger.exceptions.NoAvailableItemsException;
import dev.vdrenkov.cineledger.mappers.ItemMapper;
import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.requests.ItemRequest;
import dev.vdrenkov.cineledger.repositories.ItemRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contains business logic for item operations.
 */
@Service
public class ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    /**
     * Creates a new item service with its required collaborators.
     *
     * @param itemRepository
     *     item repository used by the operation
     * @param itemMapper
     *     item mapper used by the operation
     */
    @Autowired
    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * Creates and persists item.
     *
     * @param itemRequest
     *     request payload for the item operation
     * @return requested item value
     */
    public Item addItem(ItemRequest itemRequest) {
        log.info(String.format("An attempt to add item with name '%s' in thee database", itemRequest.getName()));

        this.itemRepository.findByName(itemRequest.getName()).ifPresent(item -> {
            log.error(String.format("Exception caught: %s", ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE));

            throw new ItemAlreadyExistsException(ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE);
        });

        log.info("An attempt to add a new item in the database");

        return itemRepository.save(new Item(itemRequest.getName(), itemRequest.getPrice(), itemRequest.getQuantity()));
    }

    /**
     * Returns item matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return item dto result
     */
    public ItemDto getItemDtoById(int id) {
        log.info(String.format("An attempt to extract item DTO with id %d from the database", id));

        return itemMapper.mapItemToItemDto(getItemById(id));
    }

    /**
     * Returns item matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested item value
     */
    public Item getItemById(int id) {
        log.info(String.format("An attempt to extract item with id %d from the database", id));

        return itemRepository.findById(id).orElseThrow(() -> {

            log.error(String.format("Exception caught: %s", ExceptionMessages.ITEM_NOT_FOUND_MESSAGE));

            throw new ItemNotFoundException(ExceptionMessages.ITEM_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns item matching the supplied criteria.
     *
     * @param itemName
     *     item name used by the operation
     * @return item dto result
     */
    public ItemDto getItemDtoByName(String itemName) {
        log.info(String.format("An attempt to extract item with name %s from the database", itemName));

        return itemMapper.mapItemToItemDto(itemRepository.findByName(itemName).orElseThrow(() -> {
            log.error(String.format("Exception caught: %s", ExceptionMessages.ITEM_NOT_FOUND_MESSAGE));

            throw new ItemNotFoundException(ExceptionMessages.ITEM_NOT_FOUND_MESSAGE);
        }));
    }

    /**
     * Returns items matching the supplied criteria.
     *
     * @param quantity
     *     quantity used by the operation
     * @param isBelow
     *     whether below should be applied
     * @return matching item dto values
     */
    public List<ItemDto> getItemsByQuantity(int quantity, Boolean isBelow) {
        final List<ItemDto> filteredItems = new ArrayList<>();
        final List<ItemDto> itemList = getAllItems();

        for (ItemDto item : itemList) {

            if (isBelow) {

                if (item.getQuantity() < quantity) {
                    filteredItems.add(item);
                }
            } else {

                if (item.getQuantity() > quantity) {

                    filteredItems.add(item);
                }
            }
        }

        return filteredItems;
    }

    /**
     * Returns all items matching the supplied criteria.
     *
     * @return matching item dto values
     */
    public List<ItemDto> getAllItems() {
        log.info("An attempt to extract all items from the database");

        final List<ItemDto> itemDtos = itemMapper.mapItemToItemDtoList(itemRepository.findAll());
        itemDtos.sort(Comparator.comparing(ItemDto::getId));

        return itemDtos;
    }

    /**
     * Executes the edit item operation for item.
     *
     * @param itemRequest
     *     request payload for the item operation
     * @param id
     *     identifier of the target resource
     * @return item dto result
     */
    public ItemDto editItem(ItemRequest itemRequest, int id) {
        final ItemDto itemDto = getItemDtoById(id);

        log.info(String.format("An attempt to update item with id %d in the database", id));

        itemRepository.save(new Item(id, itemRequest.getName(), itemRequest.getPrice(), itemRequest.getQuantity()));

        return itemDto;
    }

    /**
     * Executes the remove item operation for item.
     *
     * @param id
     *     identifier of the target resource
     * @return item dto result
     */
    public ItemDto removeItem(int id) {
        final ItemDto itemDto = getItemDtoById(id);

        itemRepository.deleteById(id);

        log.info(String.format("Item with id %d was deleted", id));

        return itemDto;
    }

    /**
     * Executes the increment item quantity operation for item.
     *
     * @param item
     *     item entity to transform
     * @return requested int value
     */
    public int incrementItemQuantity(Item item) {
        final int currentQuantity = item.getQuantity();
        final int incrementedQuantity = currentQuantity + 1;

        item.setQuantity(incrementedQuantity);

        return incrementedQuantity;
    }

    /**
     * Executes the decrement item quantity operation for item.
     *
     * @param item
     *     item entity to transform
     * @return requested int value
     */
    public int decrementItemQuantity(Item item) {
        final int currentQuantity = item.getQuantity();
        final int decrementedQuantity = currentQuantity - 1;

        item.setQuantity(decrementedQuantity);

        if (currentQuantity <= 0) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION));

            throw new NoAvailableItemsException(ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION);
        }

        return decrementedQuantity;
    }
}


