package dev.vdrenkov.cineledger.testutil.factories;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.EXPIRATION;
import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.JWT_COOKIE_NAME;
import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.JWT_PASSWORD;
import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.JWT_USERNAME;
import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.NOW;
import static dev.vdrenkov.cineledger.testutil.constants.JwtConstants.USER_ROLE;

/**
 * Provides reusable jwt fixtures for tests.
 */
public final class JwtFactory {

    private static String secret;

    private JwtFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Builds reusable jwt test data.
     *
     * @param secret
     *     secret used by the test helper
     */
    public static void setSecret(String secret) {
        JwtFactory.secret = secret;
    }

    /**
     * Returns the default jwt token fixture used in tests.
     *
     * @return test string value
     */
    public static String getDefaultJwtToken() {
        final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts
            .builder()
            .subject(JWT_USERNAME)
            .issuedAt(NOW)
            .expiration(EXPIRATION)
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
        return new UserDetails() {
            /**
             * Builds reusable jwt test data.
             * @return test collection<? extends granted authority> value
             */
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE));
            }

            /**
             * Builds reusable jwt test data.
             * @return test string value
             */
            @Override
            public String getPassword() {
                return JWT_PASSWORD;
            }

            /**
             * Builds reusable jwt test data.
             * @return test string value
             */
            @Override
            public String getUsername() {
                return JWT_USERNAME;
            }

            /**
             * Builds reusable jwt test data.
             * @return true when the asserted condition holds; otherwise false
             */
            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            /**
             * Builds reusable jwt test data.
             * @return true when the asserted condition holds; otherwise false
             */
            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            /**
             * Builds reusable jwt test data.
             * @return true when the asserted condition holds; otherwise false
             */
            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            /**
             * Builds reusable jwt test data.
             * @return true when the asserted condition holds; otherwise false
             */
            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    /**
     * Returns the default authentication fixture used in tests.
     *
     * @return test authentication value
     */
    public static Authentication getDefaultAuthentication() {
        return new Authentication() {
            /**
             * Builds reusable jwt test data.
             * @return test collection<? extends granted authority> value
             */
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            /**
             * Builds reusable jwt test data.
             * @return test object value
             */
            @Override
            public Object getCredentials() {
                return null;
            }

            /**
             * Builds reusable jwt test data.
             * @return test object value
             */
            @Override
            public Object getDetails() {
                return null;
            }

            /**
             * Builds reusable jwt test data.
             * @return test object value
             */
            @Override
            public Object getPrincipal() {
                return null;
            }

            /**
             * Builds reusable jwt test data.
             * @return true when the asserted condition holds; otherwise false
             */
            @Override
            public boolean isAuthenticated() {
                return false;
            }

            /**
             * Builds reusable jwt test data.
             *
             * @param isAuthenticated is authenticated used by the test helper
             */
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            /**
             * Builds reusable jwt test data.
             * @return test string value
             */
            @Override
            public String getName() {
                return JWT_USERNAME;
            }
        };
    }
}


