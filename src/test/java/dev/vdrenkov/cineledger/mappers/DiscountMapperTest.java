package dev.vdrenkov.cineledger.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.testutils.constants.DiscountConstants;
import dev.vdrenkov.cineledger.testutils.factories.DiscountFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DiscountMapperTest {

    @Test
    void testMapDiscountToDiscountDto_success() {
        final DiscountDto discountDto =
                DiscountMapper.mapDiscountToDiscountDto(DiscountFactory.getDefaultDiscount());

        assertEquals(DiscountConstants.ID, discountDto.id());
        assertEquals(DiscountConstants.TYPE, discountDto.type());
        assertEquals(DiscountConstants.CODE, discountDto.code());
        assertEquals(DiscountConstants.PERCENTAGE, discountDto.percentage());
    }

    @Test
    void testMapDiscountListToDiscountDtoList_success() {
        final List<DiscountDto> discountDtos =
                DiscountMapper.mapDiscountListToDiscountDtoList(
                        DiscountFactory.getDefaultDiscountList());

        assertEquals(1, discountDtos.size());
        final DiscountDto discountDto = discountDtos.getFirst();
        assertEquals(DiscountConstants.ID, discountDto.id());
        assertEquals(DiscountConstants.TYPE, discountDto.type());
        assertEquals(DiscountConstants.CODE, discountDto.code());
        assertEquals(DiscountConstants.PERCENTAGE, discountDto.percentage());
    }
}
