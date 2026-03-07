package dev.vdrenkov.cineledger.testutil.factories;

import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.requests.OrderRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutil.constants.DiscountConstants.CODE;
import static dev.vdrenkov.cineledger.testutil.constants.OrderConstants.DATE_OF_PURCHASE;
import static dev.vdrenkov.cineledger.testutil.constants.OrderConstants.ID;
import static dev.vdrenkov.cineledger.testutil.constants.OrderConstants.TOTAL_PRICE;
import static dev.vdrenkov.cineledger.testutil.factories.ItemFactory.getDefaultItemDtoList;
import static dev.vdrenkov.cineledger.testutil.factories.ItemFactory.getDefaultItemList;

/**
 * Provides reusable order fixtures for tests.
 */
public final class OrderFactory {

    private OrderFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default order request fixture used in tests.
     *
     * @return test order request value
     */
    public static OrderRequest getDefaultOrderRequest() {
        return new OrderRequest(ID, TicketFactory.getDefaultIdList(), TicketFactory.getDefaultIdList(), CODE);
    }

    /**
     * Returns the default order fixture used in tests.
     *
     * @return test order value
     */
    public static Order getDefaultOrder() {

        return new Order(ID, DATE_OF_PURCHASE, UserFactory.getDefaultUser(), TicketFactory.getDefaultTicketList(),
            getDefaultItemList(), TOTAL_PRICE);
    }

    /**
     * Returns the default order list fixture used in tests.
     *
     * @return test order values
     */
    public static List<Order> getDefaultOrderList() {
        return Collections.singletonList(getDefaultOrder());
    }

    /**
     * Returns the default order dto fixture used in tests.
     *
     * @return test order dto value
     */
    public static OrderDto getDefaultOrderDto() {
        return new OrderDto(ID, DATE_OF_PURCHASE, UserFactory.getDefaultUserDto(),
            TicketFactory.getDefaultTicketDtoList(), getDefaultItemDtoList(), TOTAL_PRICE);
    }

    /**
     * Returns the default order dto list fixture used in tests.
     *
     * @return test order dto values
     */
    public static List<OrderDto> getDefaultOrderDtoList() {
        return Collections.singletonList(getDefaultOrderDto());
    }
}


