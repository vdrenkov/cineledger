package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderRequest(
        @Positive int userId,
        @NotEmpty List<@NotNull @Positive Integer> ticketsIds,
        List<@NotNull @Positive Integer> itemsIds,
        String discountCode) {}
