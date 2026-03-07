package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Provides persistence access for role entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByName(String name);

    boolean existsByName(String name);
}


