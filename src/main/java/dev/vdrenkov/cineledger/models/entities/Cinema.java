package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cinemas")
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

    public Cinema() {}

    public Cinema(final int id, final String address, final String city, final double averageRating) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.averageRating = averageRating;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(final double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof Cinema other && id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Cinema.class.hashCode();
    }
}
