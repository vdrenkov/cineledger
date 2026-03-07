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
    Optional<Category> findByName(String name);
}


