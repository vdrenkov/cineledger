package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ProgramRequest(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
                @NotNull(message = "The program date can't be empty")
                LocalDate programDate,
        @Positive(message = "The cinema id must be positive") int cinemaId) {}
