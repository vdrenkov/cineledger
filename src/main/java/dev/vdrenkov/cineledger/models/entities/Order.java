package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a persisted order entity.
 */
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_of_purchase")
    private LocalDate dateOfPurchase;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    private List<Ticket> tickets;

    @ManyToMany
    @JoinTable(name = "orders_items", joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items;

    @Column(name = "total_price")
    private double totalPrice;

    /**
     * Creates a new order with its required collaborators.
     *
     * @param dateOfPurchase
     *     date of purchase used by the operation
     * @param user
     *     user entity to transform
     * @param tickets
     *     ticket entities or values to process
     * @param items
     *     item entities or values to process
     * @param totalPrice
     *     total price used by the operation
     */
    public Order(LocalDate dateOfPurchase, User user, List<Ticket> tickets, List<Item> items, double totalPrice) {
        this.dateOfPurchase = dateOfPurchase;
        this.user = user;
        this.tickets = tickets;
        this.items = items;
        this.totalPrice = totalPrice;
    }
}



