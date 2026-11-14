package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderRequest(
    @Positive(message = "The user id must be positive")
    @NotNull(message = "The user id can't be empty")
    int userId,

    @NotNull(message = "The tickets' ids can not be null")
    List<Integer> ticketsIds,

    List<Integer> itemsIds,
    String discountCode
) {}
