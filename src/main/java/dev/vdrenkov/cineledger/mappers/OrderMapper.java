package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.dtos.OrderItemDto;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.OrderItem;

import java.util.List;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderDto mapOrderToOrderDto(final Order order, final List<OrderItem> items) {
        final var itemDtos =
                items.stream()
                        .map(item ->
                                new OrderItemDto(
                                        item.getItemId(),
                                        item.getName(),
                                        item.getQuantity(),
                                        item.getUnitPrice(),
                                        item.getPaidTotal()))
                        .toList();
        return new OrderDto(
                order.getId(),
                order.getDateOfPurchase(),
                UserMapper.mapUserToUserDto(order.getUser()),
                TicketMapper.mapTicketToDtoList(order.getTickets()),
                itemDtos,
                order.getTotalPrice(),
                order.getDiscountPercentage());
    }
}
