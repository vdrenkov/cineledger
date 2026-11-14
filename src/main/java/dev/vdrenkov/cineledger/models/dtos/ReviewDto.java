package dev.vdrenkov.cineledger.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public record ReviewDto(
    int id,
    double rating,
    String reviewText,
    LocalDate dateModified,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    MovieDto movie,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    CinemaDto cinema,
    UserDto user
) {}
