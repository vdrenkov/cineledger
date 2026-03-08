package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Provides persistence access for cinema entities.
 */
@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    /**
     * Retrieves all cinemas in the given city.
     *
     * @param city
     *     the city to match
     * @return the cinemas located in the given city
     */
    List<Cinema> findAllByCity(String city);

    /**
     * Retrieves all cinemas on the given address.
     *
     * @param address
     *     the address to match
     * @return the cinemas registered on the given address
     */
    List<Cinema> findAllByAddress(String address);

    /**
     * Retrieves all cinemas matching both city and address.
     *
     * @param city
     *     the city to match
     * @param address
     *     the address to match
     * @return the cinemas matching both criteria
     */
    List<Cinema> findAllByCityAndAddress(String city, String address);

    /**
     * Finds a single cinema by city and address.
     *
     * @param city
     *     the city to match
     * @param address
     *     the address to match
     * @return the matching cinema when present
     */
    Optional<Cinema> findByCityAndAddress(String city, String address);
}

