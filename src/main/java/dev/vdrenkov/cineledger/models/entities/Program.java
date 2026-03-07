package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a persisted program entity.
 */
@Entity
@Table(name = "programs")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "program_date")
    private LocalDate programDate;

    @OneToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    /**
     * Creates a new program with its required collaborators.
     *
     * @param programDate
     *     program date value to evaluate
     * @param cinema
     *     cinema entity to transform
     */
    public Program(LocalDate programDate, Cinema cinema) {
        this.programDate = programDate;
        this.cinema = cinema;
    }
}



