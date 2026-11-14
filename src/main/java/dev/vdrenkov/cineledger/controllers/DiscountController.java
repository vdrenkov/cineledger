package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;
import dev.vdrenkov.cineledger.models.requests.DiscountRequest;
import dev.vdrenkov.cineledger.services.DiscountService;
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
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping(URIConstants.DISCOUNTS_PATH)
    public ResponseEntity<Void> addDiscount(@RequestBody @Valid DiscountRequest discountRequest) {
        final Discount discount = discountService.addDiscount(discountRequest);

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.DISCOUNTS_ID_PATH)
            .buildAndExpand(discount.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.DISCOUNTS_PATH)
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        final List<DiscountDto> discounts = discountService.getAllDiscountDtos();

        return ResponseEntity.ok(discounts);
    }

    @GetMapping(value = URIConstants.DISCOUNTS_PATH, params = "type")
    public ResponseEntity<DiscountDto> getDiscountByType(@RequestParam String type) {
        final DiscountDto discount = discountService.getDiscountDtoByType(type);

        return ResponseEntity.ok(discount);
    }

    @PutMapping(URIConstants.DISCOUNTS_ID_PATH)
    public ResponseEntity<DiscountDto> updateDiscount(@RequestBody @Valid DiscountRequest request, @PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final DiscountDto discountDto = discountService.updateDiscount(request, id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(discountDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.DISCOUNTS_ID_PATH)
    public ResponseEntity<DiscountDto> deleteDiscount(@PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final DiscountDto discountDto = discountService.deleteDiscount(id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(discountDto) : ResponseEntity.noContent().build();
    }
}
