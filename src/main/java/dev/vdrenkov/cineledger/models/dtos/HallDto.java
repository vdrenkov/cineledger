package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the API response payload for hall data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HallDto {
    private int id;
    private int capacity;
    private CinemaDto cinema;
}


