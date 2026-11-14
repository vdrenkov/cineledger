package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "orders_items")
@IdClass(OrderItem.Key.class)
public class OrderItem {
    @Id private int orderId;
    @Id private int itemId;
    private String name;
    private int quantity;

    @jakarta.persistence.Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @jakarta.persistence.Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal paidTotal;

    protected OrderItem() {}

    public OrderItem(
            final int orderId,
            final int itemId,
            final String name,
            final int quantity,
            final BigDecimal unitPrice,
            final BigDecimal paidTotal) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.paidTotal = paidTotal;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getPaidTotal() {
        return paidTotal;
    }

    public record Key(int orderId, int itemId) implements Serializable {}
}
