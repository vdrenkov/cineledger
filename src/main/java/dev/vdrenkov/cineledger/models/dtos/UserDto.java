package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents the API response payload for user data.
 */
@AllArgsConstructor
@Data
public class UserDto {
    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate joinDate;
    private List<RoleDto> roles;
}


