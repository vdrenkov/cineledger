package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemRequest(
        @NotNull(message = "The item name can't be empty") String name,
        @Positive(message = "The price must be positive")
                @NotNull(message = "The price can't be empty")
                @Digits(integer = 17, fraction = 2)
                BigDecimal price,
        @Positive(message = "The quantity must be positive")
                @NotNull(message = "The quantity can't be empty")
                int quantity) {}
