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

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
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

    public Order(LocalDate dateOfPurchase, User user, List<Ticket> tickets, List<Item> items, double totalPrice) {
        this.dateOfPurchase = dateOfPurchase;
        this.user = user;
        this.tickets = tickets;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public Order() {}

    public Order(final int id, final LocalDate dateOfPurchase, final User user, final List<Ticket> tickets, final List<Item> items, final double totalPrice) {
        this.id = id;
        this.dateOfPurchase = dateOfPurchase;
        this.user = user;
        this.tickets = tickets;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(final LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(final List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(final List<Item> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof Order other && id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Order.class.hashCode();
    }
}
