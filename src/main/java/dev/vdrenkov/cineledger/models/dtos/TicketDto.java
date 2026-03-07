package dev.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * Represents the API response payload for ticket data.
 */
@AllArgsConstructor
@Data
public class TicketDto {
    private int id;
    private LocalDate dateOfPurchase;
    private ProjectionDto projection;
}


