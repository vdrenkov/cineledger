package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CinemaRequest(
        @Pattern(
                        regexp = "^[A-Z0-9][A-Z0-9a-z-, .#()]*$",
                        message =
                                "The cinema's address must start with a capital letter or a digit"
                                        + " and must contain only letters, digits, dashes, commas,"
                                        + " spaces, dots, hashes and parentheses")
                @NotNull(message = "The address can't be empty")
                String address,
        @Pattern(
                        regexp = "^[A-Z0-9][A-Z0-9a-z-, .#()]*$",
                        message =
                                "The cinema's city name must start with a capital letter or a digit"
                                        + " and must contain only letters, digits, dashes, commas,"
                                        + " spaces, dots, hashes and parentheses")
                @NotNull(message = "The city's name can't be empty")
                String city) {}
