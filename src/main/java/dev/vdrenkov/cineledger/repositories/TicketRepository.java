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

    List<Ticket> findTicketByProjectionId(int projectionId);

    int countByProjectionId(int projectionId);

    List<Ticket> findTicketsByDateOfPurchaseBetween(LocalDate startDate, LocalDate endDate);
}


