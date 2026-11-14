package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.utils.constants.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private final SecretKey signingKey;
    private final JwtParser parser;

    public JwtTokenUtil(@Value("${jwt.secret}") final String secret) {
        final byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        if (keyBytes.length < 64) {
            throw new IllegalArgumentException("JWT_SECRET must contain at least 64 UTF-8 bytes");
        }

        signingKey = Keys.hmacShaKeyFor(keyBytes);
        parser = Jwts.parser()
            .verifyWith(signingKey)
            .sig()
            .clear()
            .add(Jwts.SIG.HS512)
            .and()
            .build();
    }

    public String generateToken(final UserDetails userDetails) {
        final Instant now = Instant.now();
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(JwtConstants.JWT_TOKEN_VALIDITY_SECONDS)))
            .signWith(signingKey, Jwts.SIG.HS512)
            .compact();
    }

    public String getUsernameFromToken(final String token) {
        return parseToken(token).getSubject();
    }

    public Instant getExpirationInstantFromToken(final String token) {
        return parseToken(token).getExpiration().toInstant();
    }

    public boolean validateToken(final String token, final UserDetails userDetails) {
        return getUsernameFromToken(token).equals(userDetails.getUsername());
    }

    private Claims parseToken(final String token) {
        final Claims claims = parser.parseSignedClaims(token).getPayload();

        if (claims.getSubject() == null || claims.getSubject().isBlank() || claims.getExpiration() == null) {
            throw new JwtException("Authentication token requires a subject and expiry");
        }

        return claims;
    }
}
