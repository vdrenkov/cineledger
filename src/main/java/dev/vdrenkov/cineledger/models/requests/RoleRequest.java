package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleRequest {

    @Pattern(regexp = "^[A-Z_]+$", message = "The role is allowed to contain only capital letters and underscores")
    @NotNull(message = "The role can't be empty")
    private String name;
}


