package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
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

    @OneToMany private List<Ticket> tickets;

    @Column(name = "total_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    private Integer discountPercentage = 0;

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(final Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Order(LocalDate dateOfPurchase, User user, List<Ticket> tickets, BigDecimal totalPrice) {
        this.dateOfPurchase = dateOfPurchase;
        this.user = user;
        this.tickets = tickets;
        this.totalPrice = totalPrice;
    }

    public Order() {}

    public Order(
            final int id,
            final LocalDate dateOfPurchase,
            final User user,
            final List<Ticket> tickets,
            final BigDecimal totalPrice) {
        this.id = id;
        this.dateOfPurchase = dateOfPurchase;
        this.user = user;
        this.tickets = tickets;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final BigDecimal totalPrice) {
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
