package dev.vdrenkov.cineledger.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Captures the API request payload for review operations.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewRequest {
    private double rating;
    private String reviewText;
}


