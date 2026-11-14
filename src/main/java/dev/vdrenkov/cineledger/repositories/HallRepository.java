package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Hall;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select entity from Hall entity where entity.id = :id")
    Optional<Hall> findByIdForUpdate(int id);

    List<Hall> findAllByCinemaId(int cinemaId);
}
