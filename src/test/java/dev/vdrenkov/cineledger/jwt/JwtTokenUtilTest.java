package dev.vdrenkov.cineledger.jwt;

import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.JWT_USERNAME;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.vdrenkov.cineledger.testutils.factories.JwtFactory;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

class JwtTokenUtilTest {
    private static final String SECRET =
            "gD7rL2vQ9mX4pNc8sH1tWy5kZa3uEf6jRb0qMn7cTy2wKs9hPv4xLd8nFg5rQb1sU";

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(SECRET);

    @BeforeEach
    void setUp() {
        JwtFactory.setSecret(SECRET);
    }

    @Test
    void testGetUsernameFromToken_usernameExtracted_success() {
        final String token = jwtTokenUtil.parseToken(JwtFactory.getDefaultJwtToken()).getSubject();

        assertEquals(JWT_USERNAME, token);
    }

    @Test
    void testGetExpirationInstantFromToken_dateValid_returnsTrue() {
        final Instant expiration =
                jwtTokenUtil
                        .parseToken(JwtFactory.getDefaultJwtToken())
                        .getExpiration()
                        .toInstant();

        assertTrue(expiration.isAfter(Instant.now().minusSeconds(1)));
    }

    @Test
    void testGenerateToken_tokenGenerated_success() {
        final String token = jwtTokenUtil.generateToken(JwtFactory.getDefaultUserDetails());

        assertNotNull(token);
    }

    @Test
    void parseToken_missingExpiry_rejectsToken() {
        final String token =
                Jwts.builder()
                        .subject(JWT_USERNAME)
                        .signWith(
                                Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)),
                                Jwts.SIG.HS512)
                        .compact();

        assertThrows(JwtException.class, () -> jwtTokenUtil.parseToken(token));
    }

    @Test
    void parseToken_otherAlgorithm_rejectsToken() {
        final String token =
                Jwts.builder()
                        .subject(JWT_USERNAME)
                        .expiration(java.util.Date.from(Instant.now().plusSeconds(60)))
                        .signWith(
                                Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)),
                                Jwts.SIG.HS256)
                        .compact();

        assertThrows(JwtException.class, () -> jwtTokenUtil.parseToken(token));
    }
}
