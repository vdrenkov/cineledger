package dev.vdrenkov.cineledger.testUtils.factories;

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

import static dev.vdrenkov.cineledger.testUtils.constants.JwtConstants.EXPIRATION;
import static dev.vdrenkov.cineledger.testUtils.constants.JwtConstants.JWT_COOKIE_NAME;
import static dev.vdrenkov.cineledger.testUtils.constants.JwtConstants.JWT_PASSWORD;
import static dev.vdrenkov.cineledger.testUtils.constants.JwtConstants.JWT_USERNAME;
import static dev.vdrenkov.cineledger.testUtils.constants.JwtConstants.NOW;
import static dev.vdrenkov.cineledger.testUtils.constants.JwtConstants.USER_ROLE;

public final class JwtFactory {

    private static String secret;

    private JwtFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static void setSecret(String secret) {
        JwtFactory.secret = secret;
    }

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
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE));
            }

            @Override
            public String getPassword() {
                return JWT_PASSWORD;
            }

            @Override
            public String getUsername() {
                return JWT_USERNAME;
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    public static Authentication getDefaultAuthentication() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return JWT_USERNAME;
            }
        };
    }
}


