package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Provides persistence access for program entities.
 */
@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
    /**
     * Retrieves all programs scheduled for the given date.
     *
     * @param programDate
     *     the date to match
     * @return the programs scheduled on that date
     */
    List<Program> findAllByProgramDate(LocalDate programDate);

    /**
     * Retrieves all programs that belong to the given cinema.
     *
     * @param cinemaId
     *     the cinema identifier
     * @return the programs for the cinema
     */
    List<Program> findAllByCinemaId(int cinemaId);

    /**
     * Finds the program for a cinema on a specific date.
     *
     * @param programDate
     *     the program date to match
     * @param cinemaId
     *     the cinema identifier
     * @return the matching program when present
     */
    Optional<Program> findByProgramDateAndCinemaId(LocalDate programDate, int cinemaId);
}


