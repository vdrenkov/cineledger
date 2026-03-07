package bg.vdrenkov.cineledger.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

  @NotNull(message = "The username can't be empty")
  private String username;

  @Pattern(regexp = "^.{8,}$", message = "The password should be at least 8 symbols long")
  @NotNull(message = "The password can't be empty")
  private String password;
}


