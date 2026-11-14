package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.DiscountDto;
import dev.vdrenkov.cineledger.models.entities.Discount;
import dev.vdrenkov.cineledger.models.requests.DiscountRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.CODE;
import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.PERCENTAGE;
import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.TYPE;

public final class DiscountFactory {

    private DiscountFactory() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
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
