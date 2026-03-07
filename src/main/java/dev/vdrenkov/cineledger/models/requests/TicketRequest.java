package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Captures the API request payload for ticket operations.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketRequest {

    @Positive(message = "The projection id must be positive")
    @NotNull(message = "The projection id can't be empty")
    private int projectionId;
}


