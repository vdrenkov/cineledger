package bg.vdrenkov.cineledger.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class TicketDto {

  private int id;
  private LocalDate dateOfPurchase;
  private ProjectionDto projection;
}


