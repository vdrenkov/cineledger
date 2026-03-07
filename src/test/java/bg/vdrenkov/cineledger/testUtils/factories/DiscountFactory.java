package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.dtos.DiscountDto;
import bg.vdrenkov.cineledger.models.entities.Discount;
import bg.vdrenkov.cineledger.models.requests.DiscountRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.DiscountConstants.CODE;
import static bg.vdrenkov.cineledger.testUtils.constants.DiscountConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.DiscountConstants.PERCENTAGE;
import static bg.vdrenkov.cineledger.testUtils.constants.DiscountConstants.TYPE;

public final class DiscountFactory {

    private DiscountFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static DiscountRequest getDefaultDiscountRequest() {
        return new DiscountRequest(TYPE, CODE, PERCENTAGE);
    }

    public static Discount getDefaultDiscount() {
        return new Discount(ID, TYPE, CODE, PERCENTAGE);
    }

    public static List<Discount> getDefaultDiscountList() {
        return Collections.singletonList(getDefaultDiscount());
    }

    public static DiscountDto getDefaultDiscountDto() {
        return new DiscountDto(ID, TYPE, CODE, PERCENTAGE);
    }

    public static List<DiscountDto> getDefaultDiscountDtoList() {
        return Collections.singletonList(getDefaultDiscountDto());
    }
}


