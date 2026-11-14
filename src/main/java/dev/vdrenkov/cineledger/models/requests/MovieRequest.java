package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;

public record MovieRequest(
        @NotNull(message = "The movie title can't be empty") String title,
        @NotNull(message = "The movie description can't be empty") String description,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
                @NotNull(message = "The movie release date can't be empty")
                LocalDate releaseDate,
        @NotNull(message = "The movie runtime can't be empty") Duration runtime,
        @Positive(message = "The category id must be positive")
                @NotNull(message = "The category id can't be empty")
                int categoryId) {}
