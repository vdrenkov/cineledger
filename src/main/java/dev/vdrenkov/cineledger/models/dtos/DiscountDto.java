package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the API response payload for discount data.
 */
@AllArgsConstructor
@Data
public class DiscountDto {
    private int id;
    private String type;
    private String code;
    private int percentage;
}


