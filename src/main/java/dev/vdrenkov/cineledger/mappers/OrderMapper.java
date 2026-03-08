package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps order domain models to DTO representations used by the API.
 */
public final class OrderMapper {
    private static final Logger log = LoggerFactory.getLogger(OrderMapper.class);

    private OrderMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps order values to order dto values.
     *
     * @param order
     *     order entity to transform
     * @return order dto result
     */
    public static OrderDto mapOrderToOrderDto(Order order) {
        log.info("Mapping the order with an id {} to an order DTO", order.getId());
        return new OrderDto(order.getId(), order.getDateOfPurchase(), UserMapper.mapUserToUserDto(order.getUser()),
            TicketMapper.mapTicketToDtoList(order.getTickets()), ItemMapper.mapItemToItemDtoList(order.getItems()),
            order.getTotalPrice());
    }

    /**
     * Maps order values to order dto list values.
     *
     * @param orders
     *     order entities to transform
     * @return matching order dto values
     */
    public static List<OrderDto> mapOrderToOrderDtoList(List<Order> orders) {
        return orders.stream().map(OrderMapper::mapOrderToOrderDto).toList();
    }
}


