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

import java.time.LocalTime;

/**
 * Represents a persisted projection entity.
 */
@Entity
@Table(name = "projections")
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    /**
     * Creates a new projection with its required collaborators.
     *
     * @param price
     *     price used by the operation
     * @param hall
     *     hall entity to transform
     * @param program
     *     program entity to transform
     * @param movie
     *     movie entity to transform
     * @param startTime
     *     start time used by the operation
     */
    public Projection(double price, Hall hall, Program program, Movie movie, LocalTime startTime) {
        this.price = price;
        this.hall = hall;
        this.program = program;
        this.movie = movie;
        this.startTime = startTime;
    }
}



