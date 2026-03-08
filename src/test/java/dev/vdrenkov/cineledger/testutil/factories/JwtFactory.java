package dev.vdrenkov.cineledger.testutil.factories;

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

import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.JWT_PASSWORD;
import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.JWT_USERNAME;
import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.USER_ROLE;
import static dev.vdrenkov.cineledger.utils.constants.JwtConstants.JWT_COOKIE_NAME;

/**
 * Provides reusable jwt fixtures for tests.
 */
public final class JwtFactory {

    private static String secret = "";

    private JwtFactory() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Builds reusable jwt test data.
     *
     * @param secret
     *     secret used by the test helper
     */
    public static void setSecret(final String secret) {
        JwtFactory.secret = Objects.requireNonNull(secret, "secret must not be null");
    }

    /**
     * Returns the default jwt token fixture used in tests.
     *
     * @return test string value
     */
    public static String getDefaultJwtToken() {
        final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        final Instant issuedAt = Instant.now();
        final Instant expiration = issuedAt.plus(Duration.ofMinutes(60));

        return Jwts
            .builder()
            .subject(JWT_USERNAME)
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(expiration))
            .signWith(key, Jwts.SIG.HS512)
            .compact();
    }

    /**
     * Returns the default http cookie fixture used in tests.
     *
     * @return test http cookie value
     */
    public static HttpCookie getDefaultHttpCookie() {
        return new HttpCookie(JWT_COOKIE_NAME, JwtFactory.getDefaultJwtToken());
    }

    /**
     * Returns the default cookie fixture used in tests.
     *
     * @return test cookie value
     */
    public static Cookie getDefaultCookie() {
        return new Cookie(JWT_COOKIE_NAME, JwtFactory.getDefaultJwtToken());
    }

    /**
     * Returns the default cookie array fixture used in tests.
     *
     * @return test cookie[] value
     */
    public static Cookie[] getDefaultCookieArray() {
        return new Cookie[] { getDefaultCookie() };
    }

    /**
     * Returns the default user details fixture used in tests.
     *
     * @return test user details value
     */
    public static UserDetails getDefaultUserDetails() {
        return User.withUsername(JWT_USERNAME).password(JWT_PASSWORD).authorities(getDefaultAuthorities()).build();
    }

    /**
     * Returns the default authentication fixture used in tests.
     *
     * @return test authentication value
     */
    public static Authentication getDefaultAuthentication() {
        return new UsernamePasswordAuthenticationToken(getDefaultUserDetails(), JWT_PASSWORD, getDefaultAuthorities());
    }

    private static Collection<? extends GrantedAuthority> getDefaultAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE));
    }
}


