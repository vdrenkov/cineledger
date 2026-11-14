package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.requests.ItemRequest;
import dev.vdrenkov.cineledger.services.ItemService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;

import jakarta.validation.Valid;

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

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping(URIConstants.ITEMS_PATH)
    public ResponseEntity<Void> addItem(@RequestBody @Valid ItemRequest itemRequest) {

        final Item item = itemService.addItem(itemRequest);

        URI location =
                UriComponentsBuilder.fromUriString(URIConstants.ITEMS_ID_PATH)
                        .buildAndExpand(item.getId())
                        .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.ITEMS_PATH)
    public ResponseEntity<List<ItemDto>> getAllItems() {
        final List<ItemDto> items = itemService.getAllItems();

        return ResponseEntity.ok(items);
    }

    @GetMapping(value = URIConstants.ITEMS_PATH, params = "itemName")
    public ResponseEntity<ItemDto> getItemByName(@RequestParam String itemName) {
        final ItemDto itemdto = itemService.getItemDtoByName(itemName);

        return ResponseEntity.ok(itemdto);
    }

    @GetMapping(value = URIConstants.ITEMS_PATH, params = "quantity")
    public ResponseEntity<List<ItemDto>> getItemsByQuantity(
            @RequestParam int quantity, @RequestParam Boolean isBelow) {

        final List<ItemDto> filteredItems = itemService.getItemsByQuantity(quantity, isBelow);

        return ResponseEntity.ok(filteredItems);
    }

    @PutMapping(URIConstants.ITEMS_ID_PATH)
    public ResponseEntity<ItemDto> updateItem(
            @RequestBody @Valid ItemRequest itemRequest,
            @PathVariable int id,
            @RequestParam(required = false) Boolean returnOld) {

        final ItemDto itemDto = itemService.editItem(itemRequest, id);

        return Boolean.TRUE.equals(returnOld)
                ? ResponseEntity.ok(itemDto)
                : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.ITEMS_ID_PATH)
    public ResponseEntity<ItemDto> deleteItem(
            @PathVariable int id, @RequestParam(required = false) Boolean returnOld) {

        final ItemDto itemDto = itemService.removeItem(id);

        return Boolean.TRUE.equals(returnOld)
                ? ResponseEntity.ok(itemDto)
                : ResponseEntity.noContent().build();
    }
}
