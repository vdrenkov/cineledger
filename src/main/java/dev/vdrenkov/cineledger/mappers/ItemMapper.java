package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;

import java.util.List;

public final class ItemMapper {

    private ItemMapper() {

    }

    public static ItemDto mapItemToItemDto(Item item) {

        return new ItemDto(item.getId(), item.getName(), item.getPrice(), item.getQuantity());
    }

    public static List<ItemDto> mapItemToItemDtoList(List<Item> item) {
        return item.stream().map(ItemMapper::mapItemToItemDto).toList();
    }
}
