package dev.vdrenkov.cineledger.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {

    @Positive(message = "The user id must be positive")
    @NotNull(message = "The user id can't be empty")
    private int userId;

    @NotNull(message = "The tickets' ids can not be null")
    private List<Integer> ticketsIds;

    private List<Integer> itemsIds;
    private String discountCode;
}


