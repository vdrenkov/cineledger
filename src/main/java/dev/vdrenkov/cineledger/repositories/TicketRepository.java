package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Ticket;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select entity from Ticket entity where entity.id = :id")
    Optional<Ticket> findByIdForUpdate(int id);

    List<Ticket> findTicketByProjectionId(int projectionId);

    int countByProjectionId(int projectionId);

    List<Ticket> findTicketsByDateOfPurchaseBetween(LocalDate startDate, LocalDate endDate);
}
