package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Provides persistence access for order entities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    /**
     * Retrieves all orders created by a specific user.
     *
     * @param userId
     *     the user identifier
     * @return the user's orders
     */
    List<Order> findOrderByUserId(int userId);

    /**
     * Retrieves all orders purchased between two dates, inclusive.
     *
     * @param startDate
     *     the lower purchase-date bound
     * @param endDate
     *     the upper purchase-date bound
     * @return the matching orders
     */
    List<Order> findOrdersByDateOfPurchaseBetween(LocalDate startDate, LocalDate endDate);
}


