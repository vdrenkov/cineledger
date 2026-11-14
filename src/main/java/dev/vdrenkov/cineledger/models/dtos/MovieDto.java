package dev.vdrenkov.cineledger.models.dtos;

import java.time.Duration;
import java.time.LocalDate;

public record MovieDto(
        int id,
        String title,
        double averageRating,
        String description,
        LocalDate releaseDate,
        Duration runtime,
        CategoryDto category) {}
