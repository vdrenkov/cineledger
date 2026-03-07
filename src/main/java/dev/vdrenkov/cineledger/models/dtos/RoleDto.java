package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the API response payload for role data.
 */
@AllArgsConstructor
@Data
public class RoleDto {
    private int id;
    private String name;
}


