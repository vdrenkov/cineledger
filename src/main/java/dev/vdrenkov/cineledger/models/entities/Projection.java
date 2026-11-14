package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalTime;

@Entity
@Table(name = "projections")
public class Projection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private double price;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(name = "start_time")
    private LocalTime startTime;

    public Projection(double price, Hall hall, Program program, Movie movie, LocalTime startTime) {
        this.price = price;
        this.hall = hall;
        this.program = program;
        this.movie = movie;
        this.startTime = startTime;
    }

    public Projection() {}

    public Projection(final int id, final double price, final Hall hall, final Program program, final Movie movie, final LocalTime startTime) {
        this.id = id;
        this.price = price;
        this.hall = hall;
        this.program = program;
        this.movie = movie;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(final Hall hall) {
        this.hall = hall;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(final Program program) {
        this.program = program;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(final Movie movie) {
        this.movie = movie;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(final LocalTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof Projection other && id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Projection.class.hashCode();
    }
}
