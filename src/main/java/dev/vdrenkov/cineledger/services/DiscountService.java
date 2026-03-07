package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DiscountAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.DiscountNotFoundException;
import dev.vdrenkov.cineledger.mappers.DiscountMapper;
import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;
import dev.vdrenkov.cineledger.models.requests.DiscountRequest;
import dev.vdrenkov.cineledger.repositories.DiscountRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains business logic for discount operations.
 */
@Service
public class DiscountService {
    private static final Logger log = LoggerFactory.getLogger(DiscountService.class);

    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    /**
     * Creates a new discount service with its required collaborators.
     *
     * @param discountRepository
     *     discount repository used by the operation
     * @param discountMapper
     *     discount mapper used by the operation
     */
    @Autowired
    public DiscountService(DiscountRepository discountRepository, DiscountMapper discountMapper) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
    }

    /**
     * Creates and persists discount.
     *
     * @param request
     *     request payload containing the submitted data
     * @return requested discount value
     */
    public Discount addDiscount(DiscountRequest request) {
        if (discountRepository.existsByType(request.getType()) || discountRepository.existsByCode(request.getCode())) {

            log.error(String.format("Exception thrown: %s", ExceptionMessages.DISCOUNT_ALREADY_EXISTS_MESSAGE));

            throw new DiscountAlreadyExistsException(ExceptionMessages.DISCOUNT_ALREADY_EXISTS_MESSAGE);
        }

        log.info(String.format("An attempt to add a discount with type %s", request.getType()));

        return discountRepository.save(new Discount(request.getType(), request.getCode(), request.getPercentage()));
    }

    /**
     * Returns all discounts matching the supplied criteria.
     *
     * @return matching discount values
     */
    public List<Discount> getAllDiscounts() {
        log.info("All discounts were requested from the database");

        return discountRepository.findAll();
    }

    /**
     * Returns all discount matching the supplied criteria.
     *
     * @return matching discount dto values
     */
    public List<DiscountDto> getAllDiscountDtos() {
        log.info("An attempt to map all discounts to discount DTOs");

        return discountMapper.mapDiscountListToDiscountDtoList(getAllDiscounts());
    }

    /**
     * Returns discount matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested discount value
     */
    public Discount getDiscountById(int id) {
        log.info(String.format("An attempt to get a discount with an id %d", id));

        return discountRepository.findById(id).orElseThrow(() -> {

            log.error(String.format("Exception caught: %s", ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE));

            throw new DiscountNotFoundException(ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns discount matching the supplied criteria.
     *
     * @param code
     *     code used by the operation
     * @return requested discount value
     */
    public Discount getDiscountByCode(String code) {
        log.info(String.format("An attempt to get a discount with a code %s", code));

        return discountRepository.findByCode(code).orElseThrow(() -> {

            log.error(String.format("Exception caught: %s", ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE));

            throw new DiscountNotFoundException(ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns discount matching the supplied criteria.
     *
     * @param type
     *     type used by the operation
     * @return requested discount value
     */
    public Discount getDiscountByType(String type) {
        log.info(String.format("An attempt to get a discount with a type %s", type));

        return discountRepository.findByType(type).orElseThrow(() -> {

            log.error(String.format("Exception caught: %s", ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE));

            throw new DiscountNotFoundException(ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns discount matching the supplied criteria.
     *
     * @param type
     *     type used by the operation
     * @return discount dto result
     */
    public DiscountDto getDiscountDtoByType(String type) {
        log.info("An attempt to map discount to discount DTO");

        return discountMapper.mapDiscountToDiscountDto(getDiscountByType(type));
    }

    /**
     * Updates discount and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @return discount dto result
     */
    public DiscountDto updateDiscount(DiscountRequest request, int id) {
        final DiscountDto discount = discountMapper.mapDiscountToDiscountDto(getDiscountById(id));

        discountRepository.save(new Discount(id, request.getType(), request.getCode(), request.getPercentage()));

        log.info(String.format("Discount with id %d was updated", id));

        return discount;
    }

    /**
     * Deletes discount and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @return discount dto result
     */
    public DiscountDto deleteDiscount(int id) {
        final Discount discount = getDiscountById(id);

        discountRepository.deleteById(id);

        log.info(String.format("Discount with id %d was deleted", id));

        return discountMapper.mapDiscountToDiscountDto(discount);
    }

    /**
     * Executes the apply discount operation for discount.
     *
     * @param totalPrice
     *     total price used by the operation
     * @param discountCode
     *     discount code to validate or apply
     * @return requested double value
     */
    public double applyDiscount(double totalPrice, String discountCode) {
        return totalPrice - (totalPrice * getDiscountByCode(discountCode).getPercentage() / 100);
    }
}


