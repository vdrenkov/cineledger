package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dev.vdrenkov.cineledger.models.entities.PasswordResetToken;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.repositories.PasswordResetTokenRepository;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PasswordRecoveryServiceTest {
    private static final Instant NOW = Instant.parse("2026-11-14T12:00:00Z");
    private static final String TOKEN = "a".repeat(43);
    @Mock private UserRepository userRepository;
    @Mock private PasswordResetTokenRepository tokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailService emailService;
    private PasswordRecoveryService recoveryService;
    private User user;

    @BeforeEach
    void setUp() {
        recoveryService =
                new PasswordRecoveryService(
                        userRepository,
                        tokenRepository,
                        passwordEncoder,
                        emailService,
                        Clock.fixed(NOW, ZoneOffset.UTC));
        user = UserFactory.getDefaultUser();
    }

    @Test
    void requestReset_knownAccount_storesOnlyHashAndPreservesPassword() throws Exception {
        final String previousPassword = user.getPassword();
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByIdForUpdate(user.getId())).thenReturn(Optional.of(user));
        when(tokenRepository.findById(user.getId())).thenReturn(Optional.empty());

        recoveryService.requestReset(user.getUsername());

        final var stored = ArgumentCaptor.forClass(PasswordResetToken.class);
        final var sent = ArgumentCaptor.forClass(String.class);
        verify(tokenRepository).save(stored.capture());
        verify(emailService).sendPasswordResetEmail(eq(user), sent.capture());
        assertEquals(43, sent.getValue().length());
        assertEquals(hash(sent.getValue()), stored.getValue().getTokenHash());
        assertEquals(NOW.plusSeconds(1800), stored.getValue().getExpiresAt());
        assertEquals(previousPassword, user.getPassword());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void requestReset_unknownAccount_sendsNothing() {
        when(userRepository.findUserByUsername("unknown")).thenReturn(Optional.empty());

        recoveryService.requestReset("unknown");

        verifyNoInteractions(tokenRepository, passwordEncoder, emailService);
    }

    @Test
    void requestReset_recentRequest_preservesExistingToken() {
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByIdForUpdate(user.getId())).thenReturn(Optional.of(user));
        when(tokenRepository.findById(user.getId()))
                .thenReturn(
                        Optional.of(
                                new PasswordResetToken(
                                        user.getId(),
                                        "existing",
                                        NOW.minusSeconds(59),
                                        NOW.plusSeconds(1741))));

        recoveryService.requestReset(user.getUsername());

        verify(tokenRepository, never()).save(any());
        verifyNoInteractions(emailService);
    }

    @Test
    void resetPassword_validToken_changesCredentialsAndConsumesToken() throws Exception {
        final var token = storedToken(NOW.plusSeconds(1));
        when(tokenRepository.findUserIdByTokenHash(hash(TOKEN)))
                .thenReturn(Optional.of(user.getId()));
        when(userRepository.findByIdForUpdate(user.getId())).thenReturn(Optional.of(user));
        when(tokenRepository.findById(user.getId())).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("ChangedPassword123")).thenReturn("new-password-hash");

        recoveryService.resetPassword(TOKEN, "ChangedPassword123");

        assertEquals("new-password-hash", user.getPassword());
        assertEquals(1, user.getCredentialsVersion());
        verify(tokenRepository).delete(token);
    }

    @Test
    void resetPassword_tokenExpiresNow_preservesCredentials() throws Exception {
        final String previousPassword = user.getPassword();
        when(tokenRepository.findUserIdByTokenHash(hash(TOKEN)))
                .thenReturn(Optional.of(user.getId()));
        when(userRepository.findByIdForUpdate(user.getId())).thenReturn(Optional.of(user));
        when(tokenRepository.findById(user.getId())).thenReturn(Optional.of(storedToken(NOW)));

        assertThrows(
                IllegalArgumentException.class,
                () -> recoveryService.resetPassword(TOKEN, "ChangedPassword123"));

        assertEquals(previousPassword, user.getPassword());
        verifyNoInteractions(passwordEncoder);
        verify(tokenRepository, never()).delete(any());
    }

    @Test
    void resetPassword_unknownToken_preservesCredentials() {
        when(tokenRepository.findUserIdByTokenHash(anyString())).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> recoveryService.resetPassword(TOKEN, "ChangedPassword123"));

        verifyNoInteractions(passwordEncoder, userRepository);
    }

    @Test
    void resetPassword_tokenReplacedWhileWaitingForLock_preservesCredentials() throws Exception {
        when(tokenRepository.findUserIdByTokenHash(hash(TOKEN)))
                .thenReturn(Optional.of(user.getId()));
        when(userRepository.findByIdForUpdate(user.getId())).thenReturn(Optional.of(user));
        when(tokenRepository.findById(user.getId()))
                .thenReturn(
                        Optional.of(
                                new PasswordResetToken(
                                        user.getId(),
                                        "another-token-hash",
                                        NOW,
                                        NOW.plusSeconds(1800))));

        assertThrows(
                IllegalArgumentException.class,
                () -> recoveryService.resetPassword(TOKEN, "ChangedPassword123"));

        verifyNoInteractions(passwordEncoder);
        verify(tokenRepository, never()).delete(any());
    }

    private PasswordResetToken storedToken(final Instant expiresAt) throws Exception {
        return new PasswordResetToken(user.getId(), hash(TOKEN), NOW.minusSeconds(1800), expiresAt);
    }

    private static String hash(final String token) throws Exception {
        return HexFormat.of()
                .formatHex(
                        MessageDigest.getInstance("SHA-256")
                                .digest(token.getBytes(StandardCharsets.UTF_8)));
    }
}
