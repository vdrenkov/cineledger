package dev.vdrenkov.cineledger.models.dtos;

import java.math.BigDecimal;

public record ItemDto(int id, String name, BigDecimal price, int quantity) {}
