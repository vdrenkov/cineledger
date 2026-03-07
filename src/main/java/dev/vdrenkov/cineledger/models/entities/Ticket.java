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

/**
 * Represents a persisted ticket entity.
 */
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

    /**
     * Creates a new ticket with its required collaborators.
     *
     * @param dateOfPurchase
     *     date of purchase used by the operation
     * @param projection
     *     projection entity to transform
     */
    public Ticket(LocalDate dateOfPurchase, Projection projection) {
        this.dateOfPurchase = dateOfPurchase;
        this.projection = projection;
    }
}



