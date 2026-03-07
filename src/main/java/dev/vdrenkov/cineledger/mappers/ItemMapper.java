package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps item domain models to DTO representations used by the API.
 */
@Component
public class ItemMapper {

    private static final Logger log = LoggerFactory.getLogger(ItemMapper.class);

    /**
     * Maps item values to item dto values.
     *
     * @param item
     *     item entity to transform
     * @return item dto result
     */
    public ItemDto mapItemToItemDto(Item item) {
        log.info(String.format("The item %s is being mapped to an item DTO", item.getName()));
        return new ItemDto(item.getId(), item.getName(), item.getPrice(), item.getQuantity());
    }

    /**
     * Maps item values to item dto list values.
     *
     * @param item
     *     item entity to transform
     * @return matching item dto values
     */
    public List<ItemDto> mapItemToItemDtoList(List<Item> item) {
        return item.stream().map(this::mapItemToItemDto).collect(Collectors.toList());
    }
}


