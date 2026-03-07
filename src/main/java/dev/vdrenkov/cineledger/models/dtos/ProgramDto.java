package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * Represents the API response payload for program data.
 */
@AllArgsConstructor
@Data
public class ProgramDto {
    private int id;
    private LocalDate programDate;
    private CinemaDto cinema;
}


