package dev.vdrenkov.cineledger.models.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderDto(
        int id,
        LocalDate dateOfPurchase,
        UserDto user,
        List<TicketDto> tickets,
        List<OrderItemDto> items,
        BigDecimal totalPrice,
        Integer discountPercentage) {}
