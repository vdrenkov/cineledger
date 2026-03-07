package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieRequest {

    @NotNull(message = "The movie title can't be empty")
    private String title;

    @NotNull(message = "The movie description can't be empty")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "The movie release date can't be empty")
    private LocalDate releaseDate;

    @NotNull(message = "The movie runtime can't be empty")
    private Duration runtime;

    @Positive(message = "The category id must be positive")
    @NotNull(message = "The category id can't be empty")
    private int categoryId;
}



