package dev.vdrenkov.cineledger.models.dtos;

import java.math.BigDecimal;
import java.time.LocalTime;

public record ProjectionDto(
        int id,
        BigDecimal price,
        HallDto hall,
        ProgramDto program,
        MovieDto movie,
        LocalTime startTime) {}
