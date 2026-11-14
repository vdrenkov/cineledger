package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Cinema;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {

    List<Cinema> findAllByCity(String city);

    List<Cinema> findAllByAddress(String address);

    List<Cinema> findAllByCityAndAddress(String city, String address);

    Optional<Cinema> findByCityAndAddress(String city, String address);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select entity from Cinema entity where entity.id = :id")
    Optional<Cinema> findByIdForUpdate(int id);
}
