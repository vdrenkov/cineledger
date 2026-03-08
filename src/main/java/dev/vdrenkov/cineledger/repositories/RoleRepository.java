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
    /**
     * Finds a role by its unique name.
     *
     * @param name
     *     the role name to match
     * @return the matching role when present
     */
    Optional<Role> findRoleByName(String name);

    /**
     * Checks whether a role with the given name already exists.
     *
     * @param name
     *     the role name to check
     * @return {@code true} when the role exists
     */
    boolean existsByName(String name);
}


