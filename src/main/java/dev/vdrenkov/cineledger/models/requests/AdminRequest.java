package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record AdminRequest(
        @NotNull(message = "The username can't be empty") String username,
        @Pattern(regexp = "^.{8,}$", message = "The password should be at least 8 symbols long")
                @NotNull(message = "The password can't be empty")
                String password,
        @Pattern(
                        regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                        message = "Invalid email pattern provided")
                @NotNull(message = "The email can't be empty")
                String email,
        @NotNull(message = "The first name can't be empty") String firstName,
        @NotNull(message = "The last name can't be empty") String lastName,
        @NotNull(message = "The user role names can't be empty") List<String> roleNames) {}
