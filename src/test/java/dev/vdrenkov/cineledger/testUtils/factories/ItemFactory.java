package dev.vdrenkov.cineledger.testUtils.factories;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.requests.ItemRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testUtils.constants.ItemConstants.ID;
import static dev.vdrenkov.cineledger.testUtils.constants.ItemConstants.NAME;
import static dev.vdrenkov.cineledger.testUtils.constants.ItemConstants.PRICE;
import static dev.vdrenkov.cineledger.testUtils.constants.ItemConstants.QUANTITY;

public final class ItemFactory {

    private ItemFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static Item getDefaultItem() {
        return new Item(ID, NAME, PRICE, QUANTITY);
    }

    public static List<Item> getDefaultItemList() {
        return Collections.singletonList(getDefaultItem());
    }

    public static ItemDto getDefaultItemDto() {
        return new ItemDto(ID, NAME, PRICE, QUANTITY);
    }

    public static ItemRequest getDefaultItemRequest() {
        return new ItemRequest(NAME, PRICE, QUANTITY);
    }

    public static List<ItemDto> getDefaultItemDtoList() {
        return Collections.singletonList(getDefaultItemDto());
    }
}


