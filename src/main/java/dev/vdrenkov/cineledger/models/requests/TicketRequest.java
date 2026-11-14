package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TicketRequest(
        @Positive(message = "The projection id must be positive")
                @NotNull(message = "The projection id can't be empty")
                int projectionId) {}
