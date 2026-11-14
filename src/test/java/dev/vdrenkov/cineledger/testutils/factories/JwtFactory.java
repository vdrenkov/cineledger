package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.JWT_PASSWORD;
import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.JWT_USERNAME;
import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.USER_ROLE;
import static dev.vdrenkov.cineledger.utils.constants.JwtConstants.JWT_COOKIE_NAME;

public final class JwtFactory {

    private static String secret = "";

    private JwtFactory() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static void setSecret(final String secret) {
        JwtFactory.secret = Objects.requireNonNull(secret, "secret must not be null");
    }

    public static String getDefaultJwtToken() {
        final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        final Instant issuedAt = Instant.now();
        final Instant expiration = issuedAt.plus(Duration.ofMinutes(60));

        return Jwts
            .builder()
            .subject(JWT_USERNAME)
            .claim("version", 0L)
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(expiration))
            .signWith(key, Jwts.SIG.HS512)
            .compact();
    }

    public static HttpCookie getDefaultHttpCookie() {
        return new HttpCookie(JWT_COOKIE_NAME, JwtFactory.getDefaultJwtToken());
    }

    public static Cookie getDefaultCookie() {
        return new Cookie(JWT_COOKIE_NAME, JwtFactory.getDefaultJwtToken());
    }

    public static Cookie[] getDefaultCookieArray() {
        return new Cookie[] { getDefaultCookie() };
    }

    public static UserDetails getDefaultUserDetails() {
        return new dev.vdrenkov.cineledger.jwt.AuthenticatedUser(JWT_USERNAME, JWT_PASSWORD, java.util.List.copyOf(getDefaultAuthorities()).stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).toList(), 0);
    }

    public static Authentication getDefaultAuthentication() {
        return new UsernamePasswordAuthenticationToken(getDefaultUserDetails(), JWT_PASSWORD, getDefaultAuthorities());
    }

    private static Collection<? extends GrantedAuthority> getDefaultAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE));
    }
}
