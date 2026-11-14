package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalTime;

public record ProjectionRequest(
        @Positive(message = "The price must be positive")
                @NotNull(message = "The price can't be empty")
                @jakarta.validation.constraints.Digits(integer = 17, fraction = 2)
                BigDecimal price,
        @Positive(message = "The hall id must be positive")
                @NotNull(message = "The hall id can't be empty")
                int hallId,
        @Positive(message = "The program id must be positive")
                @NotNull(message = "The program id can't be empty")
                int programId,
        @Positive(message = "The movie id must be positive")
                @NotNull(message = "The movie id can't be empty")
                int movieId,
        @NotNull(message = "The start time can't be empty") LocalTime startTime) {}
