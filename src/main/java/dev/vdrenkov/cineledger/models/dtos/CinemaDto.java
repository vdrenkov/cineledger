package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the API response payload for cinema data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CinemaDto {

    private int id;
    private String address;
    private String city;
    private double averageRating;
}


