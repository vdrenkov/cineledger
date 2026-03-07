package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Captures the API request payload for login operations.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

    @NotNull(message = "The username can't be empty")
    private String username;

    @Pattern(regexp = "^.{8,}$", message = "The password should be at least 8 symbols long")
    @NotNull(message = "The password can't be empty")
    private String password;
}


