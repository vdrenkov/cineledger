package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;

import java.util.List;

public final class DiscountMapper {

    private DiscountMapper() {}

    public static DiscountDto mapDiscountToDiscountDto(Discount discount) {

        return new DiscountDto(
                discount.getId(), discount.getType(), discount.getCode(), discount.getPercentage());
    }

    public static List<DiscountDto> mapDiscountListToDiscountDtoList(List<Discount> discounts) {
        return discounts.stream().map(DiscountMapper::mapDiscountToDiscountDto).toList();
    }
}
