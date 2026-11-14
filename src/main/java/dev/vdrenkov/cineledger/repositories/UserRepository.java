package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select user from User user where user.id = :id")
    Optional<User> findByIdForUpdate(int id);

    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findUserByEmail(String email);

    List<User> findAllByRolesName(String roleName);

    List<User> findAllByJoinDateBefore(LocalDate dateBefore);

    List<User> findAllByJoinDateAfter(LocalDate dateAfter);
}
