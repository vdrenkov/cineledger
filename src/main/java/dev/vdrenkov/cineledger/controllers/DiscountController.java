package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;
import dev.vdrenkov.cineledger.models.requests.DiscountRequest;
import dev.vdrenkov.cineledger.services.DiscountService;
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
 * Exposes REST endpoints for managing discount data.
 */
@RestController
public class DiscountController {

    private static final Logger log = LoggerFactory.getLogger(DiscountController.class);

    private final DiscountService discountService;

    /**
     * Creates a new discount controller with its required collaborators.
     *
     * @param discountService
     *     discount service used by the operation
     */
    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    /**
     * Creates and persists discount.
     *
     * @param discountRequest
     *     request payload for the discount operation
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.DISCOUNTS_PATH)
    public ResponseEntity<Void> addDiscount(@RequestBody @Valid DiscountRequest discountRequest) {
        final Discount discount = discountService.addDiscount(discountRequest);
        log.info("A request for a discount to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.DISCOUNTS_ID_PATH)
            .buildAndExpand(discount.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Returns all discounts matching the supplied criteria.
     *
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.DISCOUNTS_PATH)
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        final List<DiscountDto> discounts = discountService.getAllDiscountDtos();
        log.info("All discounts were requested from the database");

        return ResponseEntity.ok(discounts);
    }

    /**
     * Returns discount matching the supplied criteria.
     *
     * @param type
     *     type used by the operation
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.DISCOUNTS_PATH, params = "type")
    public ResponseEntity<DiscountDto> getDiscountByType(@RequestParam String type) {
        final DiscountDto discount = discountService.getDiscountDtoByType(type);
        log.info(String.format("Discount with type %s has been requested from database", type));

        return ResponseEntity.ok(discount);
    }

    /**
     * Updates discount and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.DISCOUNTS_ID_PATH)
    public ResponseEntity<DiscountDto> updateDiscount(@RequestBody @Valid DiscountRequest request, @PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        final DiscountDto discountDto = discountService.updateDiscount(request, id);
        log.info(String.format("Discount with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(discountDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes discount and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(URIConstants.DISCOUNTS_ID_PATH)
    public ResponseEntity<DiscountDto> deleteDiscount(@PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        final DiscountDto discountDto = discountService.deleteDiscount(id);
        log.info(String.format("Discount with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(discountDto) : ResponseEntity.noContent().build();
    }
}


