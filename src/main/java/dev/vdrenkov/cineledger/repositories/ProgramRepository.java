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
    List<Program> findAllByProgramDate(LocalDate programDate);

    List<Program> findAllByCinemaId(int cinemaId);

    Optional<Program> findByProgramDateAndCinemaId(LocalDate programDate, int cinemaId);
}


