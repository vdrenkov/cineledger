package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides persistence access for hall entities.
 */
@Repository
public interface HallRepository extends JpaRepository<Hall, Integer> {
    /**
     * Retrieves all halls that belong to a cinema.
     *
     * @param cinemaId
     *     the owning cinema identifier
     * @return the halls assigned to the cinema
     */
    List<Hall> findAllByCinemaId(int cinemaId);
}


