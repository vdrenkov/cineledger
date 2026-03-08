package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Provides persistence access for category entities.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * Finds a category by its unique name.
     *
     * @param name
     *     the category name to search for
     * @return the matching category when present
     */
    Optional<Category> findByName(String name);
}


