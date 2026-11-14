package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "programs")
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "program_date")
    private LocalDate programDate;

    @OneToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    public Program(LocalDate programDate, Cinema cinema) {
        this.programDate = programDate;
        this.cinema = cinema;
    }

    public Program() {}

    public Program(final int id, final LocalDate programDate, final Cinema cinema) {
        this.id = id;
        this.programDate = programDate;
        this.cinema = cinema;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public LocalDate getProgramDate() {
        return programDate;
    }

    public void setProgramDate(final LocalDate programDate) {
        this.programDate = programDate;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(final Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof Program other && id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Program.class.hashCode();
    }
}
