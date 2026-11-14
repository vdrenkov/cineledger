package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_of_purchase")
    private LocalDate dateOfPurchase;

    @ManyToOne
    @JoinColumn(name = "projection_id")
    private Projection projection;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(precision = 19, scale = 2)
    private BigDecimal paidPrice;

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(final BigDecimal paidPrice) {
        this.paidPrice = paidPrice;
    }

    public Ticket(LocalDate dateOfPurchase, Projection projection) {
        this.dateOfPurchase = dateOfPurchase;
        this.projection = projection;
        this.unitPrice = projection.getPrice();
    }

    public Ticket() {}

    public Ticket(final int id, final LocalDate dateOfPurchase, final Projection projection) {
        this.id = id;
        this.dateOfPurchase = dateOfPurchase;
        this.projection = projection;
        this.unitPrice = projection.getPrice();
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

    public Projection getProjection() {
        return projection;
    }

    public void setProjection(final Projection projection) {
        this.projection = projection;
        this.unitPrice = projection.getPrice();
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof Ticket other && id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Ticket.class.hashCode();
    }
}
