package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.requests.ItemRequest;
import dev.vdrenkov.cineledger.services.ItemService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Exposes REST endpoints for managing item data.
 */
@RestController
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    /**
     * Creates a new item controller with its required collaborators.
     *
     * @param itemService
     *     item service used by the operation
     */
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Creates and persists item.
     *
     * @param itemRequest
     *     request payload for the item operation
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.ITEMS_PATH)
    public ResponseEntity<Void> addItem(@RequestBody @Valid ItemRequest itemRequest) {
        log.info("Creation of a new item request has been submitted");
        final Item item = itemService.addItem(itemRequest);
        log.info("Created new item");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.ITEMS_ID_PATH)
            .buildAndExpand(item.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Returns all items matching the supplied criteria.
     *
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.ITEMS_PATH)
    public ResponseEntity<List<ItemDto>> getAllItems() {
        final List<ItemDto> items = itemService.getAllItems();
        log.info("All items were requested from the database");

        return ResponseEntity.ok(items);
    }

    /**
     * Returns item matching the supplied criteria.
     *
     * @param itemName
     *     item name used by the operation
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.ITEMS_PATH, params = "itemName")
    public ResponseEntity<ItemDto> getItemByName(@RequestParam String itemName) {
        final ItemDto itemdto = itemService.getItemDtoByName(itemName);
        log.info(String.format("Item with name %s has been requested from database", itemName));

        return ResponseEntity.ok(itemdto);
    }

    /**
     * Returns items matching the supplied criteria.
     *
     * @param quantity
     *     quantity used by the operation
     * @param isBelow
     *     whether below should be applied
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.ITEMS_PATH, params = "quantity")
    public ResponseEntity<List<ItemDto>> getItemsByQuantity(@RequestParam int quantity, @RequestParam Boolean isBelow) {

        final List<ItemDto> filteredItems = itemService.getItemsByQuantity(quantity, isBelow);
        log.info("Filtered items were requested from the database");

        return ResponseEntity.ok(filteredItems);
    }

    /**
     * Updates item and returns the previous state when needed.
     *
     * @param itemRequest
     *     request payload for the item operation
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.ITEMS_ID_PATH)
    public ResponseEntity<ItemDto> updateItem(@RequestBody @Valid ItemRequest itemRequest, @PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        final ItemDto itemDto = itemService.editItem(itemRequest, id);
        log.info(String.format("Item with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(itemDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes item and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(URIConstants.ITEMS_ID_PATH)
    public ResponseEntity<ItemDto> deleteItem(@PathVariable int id, @RequestParam(required = false) boolean returnOld) {

        final ItemDto itemDto = itemService.removeItem(id);
        log.info(String.format("Item with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(itemDto) : ResponseEntity.noContent().build();
    }
}



