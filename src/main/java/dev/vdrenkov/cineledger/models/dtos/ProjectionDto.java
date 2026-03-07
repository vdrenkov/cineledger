package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

/**
 * Represents the API response payload for projection data.
 */
@AllArgsConstructor
@Data
public class ProjectionDto {

    private int id;
    private double price;
    private HallDto hall;
    private ProgramDto program;
    private MovieDto movie;
    private LocalTime startTime;
}


