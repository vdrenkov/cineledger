package bg.vdrenkov.cineledger.mappers;

import bg.vdrenkov.cineledger.models.dtos.DiscountDto;
import bg.vdrenkov.cineledger.testUtils.constants.DiscountConstants;
import bg.vdrenkov.cineledger.testUtils.factories.DiscountFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountMapperTest {

    private final DiscountMapper discountMapper = new DiscountMapper();

    @Test
    public void testMapDiscountToDiscountDto_success() {
        DiscountDto discountDto = discountMapper.mapDiscountToDiscountDto(DiscountFactory.getDefaultDiscount());

        Assertions.assertEquals(discountDto.getId(), DiscountConstants.ID);
        Assertions.assertEquals(discountDto.getType(), DiscountConstants.TYPE);
        Assertions.assertEquals(discountDto.getCode(), DiscountConstants.CODE);
        Assertions.assertEquals(discountDto.getPercentage(), DiscountConstants.PERCENTAGE);
    }

    @Test
    public void testMapDiscountListToDiscountDtoList_success() {
        List<DiscountDto> discountDtos = discountMapper.mapDiscountListToDiscountDtoList(
            DiscountFactory.getDefaultDiscountList());

        assertEquals(1, discountDtos.size());
        DiscountDto discountDto = discountDtos.get(0);
        Assertions.assertEquals(discountDto.getId(), DiscountConstants.ID);
        Assertions.assertEquals(discountDto.getType(), DiscountConstants.TYPE);
        Assertions.assertEquals(discountDto.getCode(), DiscountConstants.CODE);
        Assertions.assertEquals(discountDto.getPercentage(), DiscountConstants.PERCENTAGE);
    }
}



