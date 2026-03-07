package bg.vdrenkov.cineledger.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "discounts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Discount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column
  private String type;

  @Column
  private String code;

  @Column
  private int percentage;

  public Discount(String type, String code, int percentage) {
    this.type = type;
    this.code = code;
    this.percentage = percentage;
  }
}



