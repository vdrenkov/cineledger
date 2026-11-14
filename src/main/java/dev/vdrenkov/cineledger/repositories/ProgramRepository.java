package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Program;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {

    List<Program> findAllByProgramDate(LocalDate programDate);

    List<Program> findAllByCinemaId(int cinemaId);

    Optional<Program> findByProgramDateAndCinemaId(LocalDate programDate, int cinemaId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select program from Program program where program.id = :id")
    Optional<Program> findByIdForUpdate(int id);
}
