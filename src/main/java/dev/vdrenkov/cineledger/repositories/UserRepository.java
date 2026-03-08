package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Provides persistence access for user entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Finds a user by username.
     *
     * @param username
     *     the username to match
     * @return the matching user when present
     */
    Optional<User> findUserByUsername(String username);

    /**
     * Checks whether a username is already taken.
     *
     * @param username
     *     the username to check
     * @return {@code true} when a user with that username exists
     */
    boolean existsByUsername(String username);

    /**
     * Finds a user by email address.
     *
     * @param email
     *     the email address to match
     * @return the matching user when present
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Retrieves all users that hold a role with the given name.
     *
     * @param roleName
     *     the role name to match
     * @return the users assigned to that role
     */
    List<User> findAllByRolesName(String roleName);

    /**
     * Retrieves all users who joined before the given date.
     *
     * @param dateBefore
     *     the exclusive upper join-date bound
     * @return the matching users
     */
    List<User> findAllByJoinDateBefore(LocalDate dateBefore);

    /**
     * Retrieves all users who joined after the given date.
     *
     * @param dateAfter
     *     the exclusive lower join-date bound
     * @return the matching users
     */
    List<User> findAllByJoinDateAfter(LocalDate dateAfter);
}

