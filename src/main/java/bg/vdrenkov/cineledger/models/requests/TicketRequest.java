package bg.vdrenkov.cineledger.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketRequest {

  @Positive(message = "The projection id must be positive")
  @NotNull(message = "The projection id can't be empty")
  private int projectionId;
}


