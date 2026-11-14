package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItem.Key> {
    List<OrderItem> findByOrderId(int orderId);

    List<OrderItem> findByOrderIdIn(Collection<Integer> orderIds);

    @Query(
            "select coalesce(sum(line.paidTotal), 0) from OrderItem line, Order purchase "
                    + "where line.orderId = purchase.id and line.itemId = :itemId "
                    + "and purchase.dateOfPurchase between :startDate and :endDate")
    BigDecimal incomeByItemId(int itemId, LocalDate startDate, LocalDate endDate);

    @Query(
            "select coalesce(sum(line.quantity), 0) from OrderItem line, Order purchase "
                    + "where line.orderId = purchase.id and line.itemId = :itemId "
                    + "and purchase.dateOfPurchase between :startDate and :endDate")
    long quantityByItemId(int itemId, LocalDate startDate, LocalDate endDate);
}
