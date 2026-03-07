package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a persisted cinema entity.
 */
@Entity
@Table(name = "cinemas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String address;

    @Column
    private String city;

    @Column(name = "average_rating")
    private double averageRating;

    /**
     * Creates a new cinema with its required collaborators.
     *
     * @param address
     *     address used by the operation
     * @param city
     *     city used by the operation
     * @param averageRating
     *     average rating used by the operation
     */
    public Cinema(String address, String city, double averageRating) {
        this.address = address;
        this.city = city;
        this.averageRating = averageRating;
    }
}



