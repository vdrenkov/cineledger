package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HallRequest {

    @Min(value = 21, message = "The capacity must be at least 21")
    @Max(value = 100, message = "The capacity must be at most 100")
    @NotNull(message = "The capacity can't be empty")
    private int capacity;

    @Positive(message = "The cinema id must be positive")
    @NotNull(message = "The cinema id can't be empty")
    private int cinemaId;
}



