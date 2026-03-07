package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the API response payload for category data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {

    private int id;
    private String name;
}


