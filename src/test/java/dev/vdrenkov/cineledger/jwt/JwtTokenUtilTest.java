package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.testutils.factories.JwtFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.JWT_USERNAME;
import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.NOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests jwt token util behavior.
 */
class JwtTokenUtilTest {

    private static final String SECRET = "test-secret-for-jwt-tests-should-be-at-least-thirty-two-characters";

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
     * Verifies that get Expiration Date From Token date Valid returns True.
     */
    @Test
    void testGetExpirationDateFromToken_dateValid_returnsTrue() {
        final Date date = jwtTokenUtil.getExpirationDateFromToken(JwtFactory.getDefaultJwtToken());

        assertTrue(date.after(NOW));
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
        boolean result = jwtTokenUtil.validateToken(JwtFactory.getDefaultJwtToken(),
            JwtFactory.getDefaultUserDetails());

        assertTrue(result);
    }
}



