package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps discount domain models to DTO representations used by the API.
 */
public final class DiscountMapper {
    private static final Logger log = LoggerFactory.getLogger(DiscountMapper.class);

    private DiscountMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps discount values to discount dto values.
     *
     * @param discount
     *     discount used by the operation
     * @return discount dto result
     */
    public static DiscountDto mapDiscountToDiscountDto(Discount discount) {
        log.info("Mapping the discount {} to a discount DTO", discount.getType());
        return new DiscountDto(discount.getId(), discount.getType(), discount.getCode(), discount.getPercentage());
    }

    /**
     * Maps discount list values to discount dto list values.
     *
     * @param discounts
     *     discounts used by the operation
     * @return matching discount dto values
     */
    public static List<DiscountDto> mapDiscountListToDiscountDtoList(List<Discount> discounts) {
        return discounts.stream().map(DiscountMapper::mapDiscountToDiscountDto).toList();
    }
}


