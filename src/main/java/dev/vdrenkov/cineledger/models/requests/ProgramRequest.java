package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Captures the API request payload for program operations.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "The program date can't be empty")
    private LocalDate programDate;

    @Positive(message = "The cinema id must be positive")
    private int cinemaId;
}



