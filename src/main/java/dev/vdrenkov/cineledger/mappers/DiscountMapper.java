package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps discount domain models to DTO representations used by the API.
 */
@Component
public class DiscountMapper {

    private static final Logger log = LoggerFactory.getLogger(DiscountMapper.class);

    /**
     * Maps discount values to discount dto values.
     *
     * @param discount
     *     discount used by the operation
     * @return discount dto result
     */
    public DiscountDto mapDiscountToDiscountDto(Discount discount) {
        log.info(String.format("The discount %s is being mapped to a discount DTO", discount.getType()));
        return new DiscountDto(discount.getId(), discount.getType(), discount.getCode(), discount.getPercentage());
    }

    /**
     * Maps discount list values to discount dto list values.
     *
     * @param discounts
     *     discounts used by the operation
     * @return matching discount dto values
     */
    public List<DiscountDto> mapDiscountListToDiscountDtoList(List<Discount> discounts) {
        return discounts.stream().map(this::mapDiscountToDiscountDto).collect(Collectors.toList());
    }
}


