package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Provides persistence access for item entities.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    /**
     * Finds a concession item by its unique name.
     *
     * @param itemName
     *     the item name to match
     * @return the matching item when present
     */
    Optional<Item> findByName(String itemName);
}


