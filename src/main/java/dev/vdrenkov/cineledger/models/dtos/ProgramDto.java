package dev.vdrenkov.cineledger.models.dtos;

import java.time.LocalDate;

public record ProgramDto(int id, LocalDate programDate, CinemaDto cinema) {}
