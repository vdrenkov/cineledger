package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the API response payload for item data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDto {

    private int id;
    private String name;
    private Double price;
    private int quantity;
}


