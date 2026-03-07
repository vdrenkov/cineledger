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

    public Cinema(String address, String city, double averageRating) {
        this.address = address;
        this.city = city;
        this.averageRating = averageRating;
    }
}



