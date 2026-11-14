package dev.vdrenkov.cineledger.models.dtos;

import java.time.LocalDate;

public record TicketDto(
    int id,
    LocalDate dateOfPurchase,
    ProjectionDto projection
) {}
