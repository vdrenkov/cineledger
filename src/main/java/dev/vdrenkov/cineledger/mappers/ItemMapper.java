package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps item domain models to DTO representations used by the API.
 */
public final class ItemMapper {
    private static final Logger log = LoggerFactory.getLogger(ItemMapper.class);

    private ItemMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps item values to item dto values.
     *
     * @param item
     *     item entity to transform
     * @return item dto result
     */
    public static ItemDto mapItemToItemDto(Item item) {
        log.info("Mapping the item {} to an item DTO", item.getName());
        return new ItemDto(item.getId(), item.getName(), item.getPrice(), item.getQuantity());
    }

    /**
     * Maps item values to item dto list values.
     *
     * @param item
     *     item entity to transform
     * @return matching item dto values
     */
    public static List<ItemDto> mapItemToItemDtoList(List<Item> item) {
        return item.stream().map(ItemMapper::mapItemToItemDto).toList();
    }
}


