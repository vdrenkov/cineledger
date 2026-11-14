package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.testutils.factories.JwtFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.JWT_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenUtilTest {
    private static final String SECRET = "gD7rL2vQ9mX4pNc8sH1tWy5kZa3uEf6jRb0qMn7cTy2wKs9hPv4xLd8nFg5rQb1sU";

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @BeforeEach
    void setUp() {
        JwtFactory.setSecret(SECRET);
        jwtTokenUtil.setSecret(SECRET);
    }

    @Test
    void testGetUsernameFromToken_usernameExtracted_success() {
        final String token = jwtTokenUtil.getUsernameFromToken(JwtFactory.getDefaultJwtToken());

        assertEquals(JWT_USERNAME, token);
    }

    @Test
    void testGetExpirationInstantFromToken_dateValid_returnsTrue() {
        final Instant expiration = jwtTokenUtil.getExpirationInstantFromToken(JwtFactory.getDefaultJwtToken());

        assertTrue(expiration.isAfter(Instant.now().minusSeconds(1)));
    }

    @Test
    void testGenerateToken_tokenGenerated_success() {
        final String token = jwtTokenUtil.generateToken(JwtFactory.getDefaultUserDetails());

        assertNotNull(token);
    }

    @Test
    void testValidateToken_tokenValidated_success() {
        final boolean result = jwtTokenUtil.validateToken(JwtFactory.getDefaultJwtToken(),
            JwtFactory.getDefaultUserDetails());

        assertTrue(result);
    }
}
