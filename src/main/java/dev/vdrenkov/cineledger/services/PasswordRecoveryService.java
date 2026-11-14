package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.models.entities.PasswordResetToken;
import dev.vdrenkov.cineledger.repositories.PasswordResetTokenRepository;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class PasswordRecoveryService {
    private static final Duration TOKEN_LIFETIME = Duration.ofMinutes(30);
    private static final Duration REQUEST_COOLDOWN = Duration.ofMinutes(1);
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Clock clock;
    private final SecureRandom random = new SecureRandom();

    public PasswordRecoveryService(final UserRepository userRepository, final PasswordResetTokenRepository tokenRepository,
        final PasswordEncoder passwordEncoder, final EmailService emailService, final Clock clock) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.clock = clock;
    }

    @Transactional
    public void requestReset(final String username) {
        final var account = userRepository.findUserByUsername(username);

        if (account.isEmpty()) {
            return;
        }

        final var user = userRepository.findByIdForUpdate(account.get().getId()).orElseThrow();
        final Instant now = clock.instant();
        final var existing = tokenRepository.findById(user.getId());

        if (existing.isPresent() && existing.get().getRequestedAt().plus(REQUEST_COOLDOWN).isAfter(now)) {
            return;
        }

        final byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        final String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokenRepository.save(new PasswordResetToken(user.getId(), hash(token), now, now.plus(TOKEN_LIFETIME)));
        emailService.sendPasswordResetEmail(user, token);
    }

    @Transactional
    public void resetPassword(final String token, final String password) {
        final String tokenHash = hash(token);
        final int userId = tokenRepository.findUserIdByTokenHash(tokenHash)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));
        final var user = userRepository.findByIdForUpdate(userId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));
        final var stored = tokenRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        if (!stored.getTokenHash().equals(tokenHash) || !stored.getExpiresAt().isAfter(clock.instant())) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setCredentialsVersion(user.getCredentialsVersion() + 1);
        tokenRepository.delete(stored);
    }

    private static String hash(final String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (final NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
