package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Order;

import java.util.List;

public final class OrderMapper {

    private OrderMapper() {

    }

    public static OrderDto mapOrderToOrderDto(Order order) {

        return new OrderDto(order.getId(), order.getDateOfPurchase(), UserMapper.mapUserToUserDto(order.getUser()),
            TicketMapper.mapTicketToDtoList(order.getTickets()), ItemMapper.mapItemToItemDtoList(order.getItems()),
            order.getTotalPrice());
    }

    public static List<OrderDto> mapOrderToOrderDtoList(List<Order> orders) {
        return orders.stream().map(OrderMapper::mapOrderToOrderDto).toList();
    }
}
