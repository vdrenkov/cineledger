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

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ItemService {

    @jakarta.persistence.PersistenceContext private jakarta.persistence.EntityManager entityManager;
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item addItem(ItemRequest itemRequest) {

        this.itemRepository
                .findByName(itemRequest.name())
                .ifPresent(item -> {
                    throw new ItemAlreadyExistsException(
                            ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE);
                });

        return itemRepository.save(
                new Item(itemRequest.name(), itemRequest.price(), itemRequest.quantity()));
    }

    public ItemDto getItemDtoById(int id) {

        return ItemMapper.mapItemToItemDto(getItemById(id));
    }

    public Item getItemForUpdate(final int id) {
        final Item stored =
                itemRepository
                        .findByIdForUpdate(id)
                        .orElseThrow(() ->
                                new ItemNotFoundException(
                                        ExceptionMessages.ITEM_NOT_FOUND_MESSAGE));
        entityManager.refresh(stored);
        return stored;
    }

    public Item getItemById(int id) {

        return itemRepository
                .findById(id)
                .orElseThrow(() -> {
                    return new ItemNotFoundException(ExceptionMessages.ITEM_NOT_FOUND_MESSAGE);
                });
    }

    public ItemDto getItemDtoByName(String itemName) {

        return ItemMapper.mapItemToItemDto(
                itemRepository
                        .findByName(itemName)
                        .orElseThrow(() -> {
                            return new ItemNotFoundException(
                                    ExceptionMessages.ITEM_NOT_FOUND_MESSAGE);
                        }));
    }

    public List<ItemDto> getItemsByQuantity(int quantity, boolean isBelow) {
        final List<ItemDto> filteredItems = new ArrayList<>();
        final List<ItemDto> itemList = getAllItems();

        for (ItemDto item : itemList) {

            if (isBelow) {

                if (item.quantity() < quantity) {
                    filteredItems.add(item);
                }
            } else {

                if (item.quantity() > quantity) {

                    filteredItems.add(item);
                }
            }
        }

        return filteredItems;
    }

    public List<ItemDto> getAllItems() {

        final List<ItemDto> itemDtos =
                new ArrayList<>(ItemMapper.mapItemToItemDtoList(itemRepository.findAll()));
        itemDtos.sort(Comparator.comparing(ItemDto::id));

        return itemDtos;
    }

    public ItemDto editItem(ItemRequest itemRequest, int id) {
        final ItemDto itemDto = getItemDtoById(id);

        itemRepository.save(
                new Item(id, itemRequest.name(), itemRequest.price(), itemRequest.quantity()));

        return itemDto;
    }

    public ItemDto removeItem(int id) {
        final ItemDto itemDto = getItemDtoById(id);

        itemRepository.deleteById(id);

        return itemDto;
    }

    public static int incrementItemQuantity(Item item) {
        final int currentQuantity = item.getQuantity();
        final int incrementedQuantity = currentQuantity + 1;

        item.setQuantity(incrementedQuantity);

        return incrementedQuantity;
    }

    public static int decrementItemQuantity(Item item) {
        final int currentQuantity = item.getQuantity();
        final int decrementedQuantity = currentQuantity - 1;

        if (currentQuantity <= 0) {

            throw new NoAvailableItemsException(ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION);
        }

        item.setQuantity(decrementedQuantity);
        return decrementedQuantity;
    }
}
