package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_of_purchase")
    private LocalDate dateOfPurchase;

    @ManyToOne
    @JoinColumn(name = "projection_id")
    private Projection projection;

    public Ticket(LocalDate dateOfPurchase, Projection projection) {
        this.dateOfPurchase = dateOfPurchase;
        this.projection = projection;
    }
}



