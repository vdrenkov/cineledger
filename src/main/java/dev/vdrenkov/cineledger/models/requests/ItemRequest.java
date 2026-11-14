package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemRequest(
    @NotNull(message = "The item name can't be empty")
    String name,

    @Positive(message = "The price must be positive")
    @NotNull(message = "The price can't be empty")
    double price,

    @Positive(message = "The quantity must be positive")
    @NotNull(message = "The quantity can't be empty")
    int quantity
) {}
