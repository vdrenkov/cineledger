package dev.vdrenkov.cineledger.models.dtos;

import java.time.LocalTime;

public record ProjectionDto(
    int id,
    double price,
    HallDto hall,
    ProgramDto program,
    MovieDto movie,
    LocalTime startTime
) {}
