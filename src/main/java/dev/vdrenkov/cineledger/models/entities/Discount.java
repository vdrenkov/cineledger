package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column private String type;

    @Column private String code;

    @Column private int percentage;

    public Discount(String type, String code, int percentage) {
        this.type = type;
        this.code = code;
        this.percentage = percentage;
    }

    public Discount() {}

    public Discount(final int id, final String type, final String code, final int percentage) {
        this.id = id;
        this.type = type;
        this.code = code;
        this.percentage = percentage;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(final int percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof Discount other && id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Discount.class.hashCode();
    }
}
