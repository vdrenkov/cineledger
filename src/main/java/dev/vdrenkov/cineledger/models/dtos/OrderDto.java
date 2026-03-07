package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents the API response payload for order data.
 */
@AllArgsConstructor
@Data
public class OrderDto {
    private int id;
    private LocalDate dateOfPurchase;
    private UserDto user;
    private List<TicketDto> tickets;
    private List<ItemDto> items;
    private double totalPrice;
}


