package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a persisted discount entity.
 */
@Entity
@Table(name = "discounts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String type;

    @Column
    private String code;

    @Column
    private int percentage;

    /**
     * Creates a new discount with its required collaborators.
     *
     * @param type
     *     type used by the operation
     * @param code
     *     code used by the operation
     * @param percentage
     *     percentage used by the operation
     */
    public Discount(String type, String code, int percentage) {
        this.type = type;
        this.code = code;
        this.percentage = percentage;
    }
}



