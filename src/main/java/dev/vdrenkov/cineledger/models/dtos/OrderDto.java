package dev.vdrenkov.cineledger.models.dtos;

import java.time.LocalDate;
import java.util.List;

public record OrderDto(
    int id,
    LocalDate dateOfPurchase,
    UserDto user,
    List<TicketDto> tickets,
    List<ItemDto> items,
    double totalPrice
) {}
