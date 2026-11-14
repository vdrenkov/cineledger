package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Ticket;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    @Query(
            "select coalesce(sum(ticket.paidPrice), 0) from Order purchase join purchase.tickets"
                + " ticket where ticket.projection.hall.cinema.id = :id and purchase.dateOfPurchase"
                + " between :startDate and :endDate")
    BigDecimal incomeByCinemaId(int id, LocalDate startDate, LocalDate endDate);

    @Query(
            "select coalesce(sum(ticket.paidPrice), 0) from Order purchase join purchase.tickets"
                    + " ticket where ticket.projection.hall.id = :id and purchase.dateOfPurchase"
                    + " between :startDate and :endDate")
    BigDecimal incomeByHallId(int id, LocalDate startDate, LocalDate endDate);

    @Query(
            "select coalesce(sum(ticket.paidPrice), 0) from Order purchase join purchase.tickets"
                    + " ticket where ticket.projection.movie.id = :id and purchase.dateOfPurchase"
                    + " between :startDate and :endDate")
    BigDecimal incomeByMovieId(int id, LocalDate startDate, LocalDate endDate);

    @Query(
            "select count(ticket) from Order purchase join purchase.tickets ticket where"
                + " ticket.projection.movie.category.id = :categoryId and purchase.dateOfPurchase"
                + " between :startDate and :endDate")
    long purchasedCountByCategoryId(int categoryId, LocalDate startDate, LocalDate endDate);

    @Query(
            "select count(ticket) from Order purchase join purchase.tickets ticket where"
                    + " ticket.projection.movie.id = :movieId and purchase.dateOfPurchase between"
                    + " :startDate and :endDate")
    long purchasedCountByMovieId(int movieId, LocalDate startDate, LocalDate endDate);
}
