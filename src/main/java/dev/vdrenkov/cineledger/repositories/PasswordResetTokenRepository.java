package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.PasswordResetToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    @Query("select token.userId from PasswordResetToken token where token.tokenHash = :tokenHash")
    Optional<Integer> findUserIdByTokenHash(String tokenHash);
}
