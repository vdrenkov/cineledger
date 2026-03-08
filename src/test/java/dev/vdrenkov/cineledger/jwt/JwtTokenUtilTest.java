package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.testutil.factories.JwtFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.JWT_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests jwt token util behavior.
 */
class JwtTokenUtilTest {
    private static final String SECRET = "gD7rL2vQ9mX4pNc8sH1tWy5kZa3uEf6jRb0qMn7cTy2wKs9hPv4xLd8nFg5rQb1sU";

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setUp() {
        JwtFactory.setSecret(SECRET);
        jwtTokenUtil.setSecret(SECRET);
    }

    /**
     * Verifies that get Username From Token username Extracted success.
     */
    @Test
    void testGetUsernameFromToken_usernameExtracted_success() {
        final String token = jwtTokenUtil.getUsernameFromToken(JwtFactory.getDefaultJwtToken());

        assertEquals(JWT_USERNAME, token);
    }

    /**
     * Verifies that get Expiration Instant From Token date Valid returns True.
     */
    @Test
    void testGetExpirationInstantFromToken_dateValid_returnsTrue() {
        final Instant expiration = jwtTokenUtil.getExpirationInstantFromToken(JwtFactory.getDefaultJwtToken());

        assertTrue(expiration.isAfter(Instant.now().minusSeconds(1)));
    }

    /**
     * Verifies that generate Token token Generated success.
     */
    @Test
    void testGenerateToken_tokenGenerated_success() {
        final String token = jwtTokenUtil.generateToken(JwtFactory.getDefaultUserDetails());

        assertNotNull(token);
    }

    /**
     * Verifies that validate Token token Validated success.
     */
    @Test
    void testValidateToken_tokenValidated_success() {
        final boolean result = jwtTokenUtil.validateToken(JwtFactory.getDefaultJwtToken(),
            JwtFactory.getDefaultUserDetails());

        assertTrue(result);
    }
}



