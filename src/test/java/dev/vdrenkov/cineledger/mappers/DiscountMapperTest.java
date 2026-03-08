package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.testutil.constants.DiscountConstants;
import dev.vdrenkov.cineledger.testutil.factories.DiscountFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

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

        assertEquals(DiscountConstants.ID, discountDto.getId());
        assertEquals(DiscountConstants.TYPE, discountDto.getType());
        assertEquals(DiscountConstants.CODE, discountDto.getCode());
        assertEquals(DiscountConstants.PERCENTAGE, discountDto.getPercentage());
    }

    /**
     * Verifies that map Discount List To Discount DTO List success.
     */
    @Test
    void testMapDiscountListToDiscountDtoList_success() {
        final List<DiscountDto> discountDtos = discountMapper.mapDiscountListToDiscountDtoList(
            DiscountFactory.getDefaultDiscountList());

        assertEquals(1, discountDtos.size());
        final DiscountDto discountDto = discountDtos.getFirst();
        assertEquals(DiscountConstants.ID, discountDto.getId());
        assertEquals(DiscountConstants.TYPE, discountDto.getType());
        assertEquals(DiscountConstants.CODE, discountDto.getCode());
        assertEquals(DiscountConstants.PERCENTAGE, discountDto.getPercentage());
    }
}



