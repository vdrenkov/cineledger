package dev.vdrenkov.cineledger.models.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TicketDto(
        int id,
        LocalDate dateOfPurchase,
        ProjectionDto projection,
        BigDecimal unitPrice,
        BigDecimal paidPrice) {}
