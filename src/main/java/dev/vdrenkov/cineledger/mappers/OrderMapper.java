package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps order domain models to DTO representations used by the API.
 */
@Component
public class OrderMapper {
    private static final Logger log = LoggerFactory.getLogger(OrderMapper.class);

    private final UserMapper userMapper;
    private final TicketMapper ticketMapper;
    private final ItemMapper itemMapper;

    /**
     * Creates a new order mapper with its required collaborators.
     *
     * @param userMapper
     *     user mapper used by the operation
     * @param ticketMapper
     *     ticket mapper used by the operation
     * @param itemMapper
     *     item mapper used by the operation
     */
    @Autowired
    public OrderMapper(UserMapper userMapper, TicketMapper ticketMapper, ItemMapper itemMapper) {
        this.userMapper = userMapper;
        this.ticketMapper = ticketMapper;
        this.itemMapper = itemMapper;
    }

    /**
     * Maps order values to order dto values.
     *
     * @param order
     *     order entity to transform
     * @return order dto result
     */
    public OrderDto mapOrderToOrderDto(Order order) {
        log.info(String.format("The order with an id %d is being mapped to an order DTO", order.getId()));
        return new OrderDto(order.getId(), order.getDateOfPurchase(), userMapper.mapUserToUserDto(order.getUser()),
            ticketMapper.mapTicketToDtoList(order.getTickets()), itemMapper.mapItemToItemDtoList(order.getItems()),
            order.getTotalPrice());
    }

    /**
     * Maps order values to order dto list values.
     *
     * @param orders
     *     order entities to transform
     * @return matching order dto values
     */
    public List<OrderDto> mapOrderToOrderDtoList(List<Order> orders) {
        return orders.stream().map(this::mapOrderToOrderDto).collect(Collectors.toList());
    }
}


