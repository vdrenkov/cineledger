package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Provides persistence access for ticket entities.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    /**
     * Retrieves all tickets for a projection.
     *
     * @param projectionId
     *     the projection identifier
     * @return the tickets issued for the projection
     */
    List<Ticket> findTicketByProjectionId(int projectionId);

    /**
     * Counts the tickets issued for a projection.
     *
     * @param projectionId
     *     the projection identifier
     * @return the number of issued tickets
     */
    int countByProjectionId(int projectionId);

    /**
     * Retrieves all tickets purchased between two dates, inclusive.
     *
     * @param startDate
     *     the lower purchase-date bound
     * @param endDate
     *     the upper purchase-date bound
     * @return the matching tickets
     */
    List<Ticket> findTicketsByDateOfPurchaseBetween(LocalDate startDate, LocalDate endDate);
}


