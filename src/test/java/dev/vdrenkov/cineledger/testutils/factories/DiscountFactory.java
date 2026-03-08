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

/**
 * Provides reusable discount fixtures for tests.
 */
public final class DiscountFactory {

    private DiscountFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default discount request fixture used in tests.
     *
     * @return test discount request value
     */
    public static DiscountRequest getDefaultDiscountRequest() {
        return new DiscountRequest(TYPE, CODE, PERCENTAGE);
    }

    /**
     * Returns the default discount fixture used in tests.
     *
     * @return test discount value
     */
    public static Discount getDefaultDiscount() {
        return new Discount(ID, TYPE, CODE, PERCENTAGE);
    }

    /**
     * Returns the default discount list fixture used in tests.
     *
     * @return test discount values
     */
    public static List<Discount> getDefaultDiscountList() {
        return Collections.singletonList(getDefaultDiscount());
    }

    /**
     * Returns the default discount dto fixture used in tests.
     *
     * @return test discount dto value
     */
    public static DiscountDto getDefaultDiscountDto() {
        return new DiscountDto(ID, TYPE, CODE, PERCENTAGE);
    }

    /**
     * Returns the default discount dto list fixture used in tests.
     *
     * @return test discount dto values
     */
    public static List<DiscountDto> getDefaultDiscountDtoList() {
        return Collections.singletonList(getDefaultDiscountDto());
    }
}


