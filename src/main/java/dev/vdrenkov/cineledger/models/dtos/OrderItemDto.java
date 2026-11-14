package dev.vdrenkov.cineledger.models.dtos;

import java.math.BigDecimal;

public record OrderItemDto(
        int id, String name, int quantity, BigDecimal unitPrice, BigDecimal paidTotal) {}
