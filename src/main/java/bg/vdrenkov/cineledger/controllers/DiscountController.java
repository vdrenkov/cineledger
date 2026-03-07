package bg.vdrenkov.cineledger.controllers;

import bg.vdrenkov.cineledger.models.dtos.DiscountDto;
import bg.vdrenkov.cineledger.models.entities.Discount;
import bg.vdrenkov.cineledger.models.requests.DiscountRequest;
import bg.vdrenkov.cineledger.services.DiscountService;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
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

@RestController
public class DiscountController {

    private static final Logger log = LoggerFactory.getLogger(DiscountController.class);

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping(URIConstants.DISCOUNTS_PATH)
    public ResponseEntity<Void> addDiscount(@RequestBody @Valid DiscountRequest discountRequest) {
        Discount discount = discountService.addDiscount(discountRequest);
        log.info("A request for a discount to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.DISCOUNTS_ID_PATH)
            .buildAndExpand(discount.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.DISCOUNTS_PATH)
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        List<DiscountDto> discounts = discountService.getAllDiscountDtos();
        log.info("All discounts were requested from the database");

        return ResponseEntity.ok(discounts);
    }

    @GetMapping(value = URIConstants.DISCOUNTS_PATH, params = "type")
    public ResponseEntity<DiscountDto> getDiscountByType(@RequestParam String type) {
        DiscountDto discount = discountService.getDiscountDtoByType(type);
        log.info(String.format("Discount with type %s has been requested from database", type));

        return ResponseEntity.ok(discount);
    }

    @PutMapping(URIConstants.DISCOUNTS_ID_PATH)
    public ResponseEntity<DiscountDto> updateDiscount(@RequestBody @Valid DiscountRequest request, @PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        DiscountDto discountDto = discountService.updateDiscount(request, id);
        log.info(String.format("Discount with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(discountDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.DISCOUNTS_ID_PATH)
    public ResponseEntity<DiscountDto> deleteDiscount(@PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        DiscountDto discountDto = discountService.deleteDiscount(id);
        log.info(String.format("Discount with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(discountDto) : ResponseEntity.noContent().build();
    }
}


