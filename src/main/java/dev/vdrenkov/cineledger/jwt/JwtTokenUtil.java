package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.utils.constants.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    @Serial
    private static final long serialVersionUID = 4910407962728938714L;

    @Value("${jwt.secret}")
    private String secret;

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public String getUsernameFromToken(final String token) {

        return getClaimFromToken(token, Claims::getSubject);
    }

    public Instant getExpirationInstantFromToken(final String token) {

        return getClaimFromToken(token, claims -> claims.getExpiration().toInstant());
    }

    public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(final String token) {

        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(final String token) {
        final Instant expiration = getExpirationInstantFromToken(token);

        return expiration.isBefore(Instant.now());
    }

    public String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();

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

    public boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = getUsernameFromToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
