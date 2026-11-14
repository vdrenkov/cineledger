package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column private int capacity;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    public Hall(int capacity, Cinema cinema) {
        this.capacity = capacity;
        this.cinema = cinema;
    }

    public Hall() {}

    public Hall(final int id, final int capacity, final Cinema cinema) {
        this.id = id;
        this.capacity = capacity;
        this.cinema = cinema;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(final int capacity) {
        this.capacity = capacity;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(final Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof Hall other && id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Hall.class.hashCode();
    }
}
