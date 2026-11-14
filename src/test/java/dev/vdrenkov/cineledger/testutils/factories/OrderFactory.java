package dev.vdrenkov.cineledger.testutils.factories;

import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.CODE;
import static dev.vdrenkov.cineledger.testutils.constants.OrderConstants.DATE_OF_PURCHASE;
import static dev.vdrenkov.cineledger.testutils.constants.OrderConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.OrderConstants.TOTAL_PRICE;

import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.requests.OrderRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

public final class OrderFactory {

    private OrderFactory() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static OrderRequest getDefaultOrderRequest() {
        return new OrderRequest(
                ID, TicketFactory.getDefaultIdList(), TicketFactory.getDefaultIdList(), CODE);
    }

    public static Order getDefaultOrder() {

        return new Order(
                ID,
                DATE_OF_PURCHASE,
                UserFactory.getDefaultUser(),
                TicketFactory.getDefaultTicketList(),
                TOTAL_PRICE);
    }

    public static List<Order> getDefaultOrderList() {
        return Collections.singletonList(getDefaultOrder());
    }

    public static OrderDto getDefaultOrderDto() {
        return new OrderDto(
                ID,
                DATE_OF_PURCHASE,
                UserFactory.getDefaultUserDto(),
                TicketFactory.getDefaultTicketDtoList(),
                List.of(
                        new dev.vdrenkov.cineledger.models.dtos.OrderItemDto(
                                1,
                                "Name",
                                1,
                                dev.vdrenkov.cineledger.testutils.constants.ItemConstants.PRICE,
                                dev.vdrenkov.cineledger.testutils.constants.ItemConstants.PRICE)),
                TOTAL_PRICE,
                0);
    }

    public static List<OrderDto> getDefaultOrderDtoList() {
        return Collections.singletonList(getDefaultOrderDto());
    }
}
