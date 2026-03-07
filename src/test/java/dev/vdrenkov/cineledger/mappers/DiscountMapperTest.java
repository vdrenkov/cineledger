package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.testutils.constants.DiscountConstants;
import dev.vdrenkov.cineledger.testutils.factories.DiscountFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests discount mapper behavior.
 */
class DiscountMapperTest {

    private final DiscountMapper discountMapper = new DiscountMapper();

    /**
     * Verifies that map Discount To Discount DTO success.
     */
    @Test
    void testMapDiscountToDiscountDto_success() {
        final DiscountDto discountDto = discountMapper.mapDiscountToDiscountDto(DiscountFactory.getDefaultDiscount());

        Assertions.assertEquals(discountDto.getId(), DiscountConstants.ID);
        Assertions.assertEquals(discountDto.getType(), DiscountConstants.TYPE);
        Assertions.assertEquals(discountDto.getCode(), DiscountConstants.CODE);
        Assertions.assertEquals(discountDto.getPercentage(), DiscountConstants.PERCENTAGE);
    }

    /**
     * Verifies that map Discount List To Discount DTO List success.
     */
    @Test
    void testMapDiscountListToDiscountDtoList_success() {
        List<DiscountDto> discountDtos = discountMapper.mapDiscountListToDiscountDtoList(
            DiscountFactory.getDefaultDiscountList());

        assertEquals(1, discountDtos.size());
        final DiscountDto discountDto = discountDtos.get(0);
        Assertions.assertEquals(discountDto.getId(), DiscountConstants.ID);
        Assertions.assertEquals(discountDto.getType(), DiscountConstants.TYPE);
        Assertions.assertEquals(discountDto.getCode(), DiscountConstants.CODE);
        Assertions.assertEquals(discountDto.getPercentage(), DiscountConstants.PERCENTAGE);
    }
}



