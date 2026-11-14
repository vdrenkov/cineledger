package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Order;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select entity from Order entity where entity.id = :id")
    Optional<Order> findByIdForUpdate(int id);

    @Query(
            "select coalesce(sum(purchase.totalPrice), 0) from Order purchase where"
                + " purchase.user.id = :userId and purchase.dateOfPurchase between :startDate and"
                + " :endDate")
    java.math.BigDecimal incomeByUserId(int userId, LocalDate startDate, LocalDate endDate);

    List<Order> findOrderByUserId(int userId);

    List<Order> findOrdersByDateOfPurchaseBetween(LocalDate startDate, LocalDate endDate);
}
