package dev.vdrenkov.cineledger.models.dtos;

import java.time.LocalDate;
import java.util.List;

public record UserDto(
    int id,
    String username,
    String email,
    String firstName,
    String lastName,
    LocalDate joinDate,
    List<RoleDto> roles
) {}
