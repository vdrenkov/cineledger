package dev.vdrenkov.cineledger.models.dtos;

public record ItemDto(
    int id,
    String name,
    Double price,
    int quantity
) {}
