package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    @Id private int userId;
    private String tokenHash;
    private Instant requestedAt;
    private Instant expiresAt;

    protected PasswordResetToken() {}

    public PasswordResetToken(
            final int userId,
            final String tokenHash,
            final Instant requestedAt,
            final Instant expiresAt) {
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.requestedAt = requestedAt;
        this.expiresAt = expiresAt;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
