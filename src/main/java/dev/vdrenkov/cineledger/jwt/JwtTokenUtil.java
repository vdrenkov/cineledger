package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.utils.constants.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Generates, parses, and validates JWT tokens for authenticated users.
 */
@Component
public class JwtTokenUtil implements Serializable {
    private final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Overrides the JWT signing secret, primarily for tests.
     *
     * @param secret
     *     JWT signing secret
     */
    public void setSecret(final String secret) {
        this.secret = secret;
    }

    /**
     * Extracts the username stored in the supplied JWT token.
     *
     * @param token
     *     JWT token value
     * @return resulting string value
     */
    public String getUsernameFromToken(final String token) {
        log.info("An attempt to extract the username from the JWT token");
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration timestamp from the supplied JWT token.
     *
     * @param token
     *     JWT token value
     * @return requested instant value
     */
    public Instant getExpirationInstantFromToken(final String token) {
        log.info("An attempt to extract the expiration date from the JWT token");
        return getClaimFromToken(token, claims -> claims.getExpiration().toInstant());
    }

    /**
     * Extracts a claim from the supplied JWT token.
     *
     * @param token
     *     JWT token value
     * @param claimsResolver
     *     function used to extract a claim from the token
     * @return requested t value
     */
    public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        log.info("An attempt to extract the claim from the JWT token");
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(final String token) {
        log.info("An attempt to extract all claims from the JWT token");
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(final String token) {
        final Instant expiration = getExpirationInstantFromToken(token);
        log.info("An attempt to check whether the JWT has expired");
        return expiration.isBefore(Instant.now());
    }

    /**
     * Generates a signed JWT token for the supplied user.
     *
     * @param userDetails
     *     authenticated user details
     * @return resulting string value
     */
    public String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        log.info("An attempt to generate a JWT token");
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(final Map<String, Object> claims, final String subject) {
        final Instant now = Instant.now();
        final Instant expiration = now.plusMillis(JwtConstants.JWT_TOKEN_VALIDITY_MILLIS);

        return Jwts
            .builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(java.util.Date.from(now))
            .expiration(java.util.Date.from(expiration))
            .signWith(getSigningKey(), Jwts.SIG.HS512)
            .compact();
    }

    /**
     * Validates that the supplied token belongs to the given user and is still active.
     *
     * @param token
     *     JWT token value
     * @param userDetails
     *     authenticated user details
     * @return true when the requested condition holds; otherwise false
     */
    public boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        log.info("An attempt to validate a JWT token");
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}

