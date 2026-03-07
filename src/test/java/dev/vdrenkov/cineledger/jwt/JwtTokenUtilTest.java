package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.testUtils.factories.JwtFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static dev.vdrenkov.cineledger.testUtils.constants.JwtConstants.JWT_USERNAME;
import static dev.vdrenkov.cineledger.testUtils.constants.JwtConstants.NOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtTokenUtilTest {

    private static final String SECRET = "test-secret-for-jwt-tests-should-be-at-least-thirty-two-characters";

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @BeforeEach
    public void setUp() {
        JwtFactory.setSecret(SECRET);
        jwtTokenUtil.setSecret(SECRET);
    }

    @Test
    public void testGetUsernameFromToken_usernameExtracted_success() {
        String token = jwtTokenUtil.getUsernameFromToken(JwtFactory.getDefaultJwtToken());

        assertEquals(JWT_USERNAME, token);
    }

    @Test
    public void testGetExpirationDateFromToken_dateValid_returnsTrue() {
        Date date = jwtTokenUtil.getExpirationDateFromToken(JwtFactory.getDefaultJwtToken());

        assertTrue(date.after(NOW));
    }

    @Test
    public void testGenerateToken_tokenGenerated_success() {
        String token = jwtTokenUtil.generateToken(JwtFactory.getDefaultUserDetails());

        assertNotNull(token);
    }

    @Test
    public void testValidateToken_tokenValidated_success() {
        boolean result = jwtTokenUtil.validateToken(JwtFactory.getDefaultJwtToken(),
            JwtFactory.getDefaultUserDetails());

        assertTrue(result);
    }
}



