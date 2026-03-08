package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.requests.ItemRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.ItemConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.ItemConstants.NAME;
import static dev.vdrenkov.cineledger.testutils.constants.ItemConstants.PRICE;
import static dev.vdrenkov.cineledger.testutils.constants.ItemConstants.QUANTITY;

/**
 * Provides reusable item fixtures for tests.
 */
public final class ItemFactory {

    private ItemFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default item fixture used in tests.
     *
     * @return test item value
     */
    public static Item getDefaultItem() {
        return new Item(ID, NAME, PRICE, QUANTITY);
    }

    /**
     * Returns the default item list fixture used in tests.
     *
     * @return test item values
     */
    public static List<Item> getDefaultItemList() {
        return Collections.singletonList(getDefaultItem());
    }

    /**
     * Returns the default item dto fixture used in tests.
     *
     * @return test item dto value
     */
    public static ItemDto getDefaultItemDto() {
        return new ItemDto(ID, NAME, PRICE, QUANTITY);
    }

    /**
     * Returns the default item request fixture used in tests.
     *
     * @return test item request value
     */
    public static ItemRequest getDefaultItemRequest() {
        return new ItemRequest(NAME, PRICE, QUANTITY);
    }

    /**
     * Returns the default item dto list fixture used in tests.
     *
     * @return test item dto values
     */
    public static List<ItemDto> getDefaultItemDtoList() {
        return Collections.singletonList(getDefaultItemDto());
    }
}


