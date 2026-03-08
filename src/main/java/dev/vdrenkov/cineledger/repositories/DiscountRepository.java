package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Provides persistence access for discount entities.
 */
@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    /**
     * Checks whether a discount with the given type already exists.
     *
     * @param type
     *     the discount type to check
     * @return {@code true} when a discount with that type exists
     */
    boolean existsByType(String type);

    /**
     * Checks whether a discount with the given code already exists.
     *
     * @param code
     *     the discount code to check
     * @return {@code true} when a discount with that code exists
     */
    boolean existsByCode(String code);

    /**
     * Finds a discount by its type.
     *
     * @param type
     *     the discount type to match
     * @return the matching discount when present
     */
    Optional<Discount> findByType(String type);

    /**
     * Finds a discount by its code.
     *
     * @param code
     *     the discount code to match
     * @return the matching discount when present
     */
    Optional<Discount> findByCode(String code);
}

