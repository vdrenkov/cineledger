package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.utils.constants.JwtConstants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieUtil {

    private final JwtTokenUtil tokenUtil;
    private final boolean secureCookie;
    private final String sameSitePolicy;

    public JwtCookieUtil(
            final JwtTokenUtil tokenUtil,
            @Value("${jwt.cookie.secure:true}") final boolean secureCookie,
            @Value("${jwt.cookie.same-site:Lax}") final String sameSitePolicy) {
        this.tokenUtil = tokenUtil;
        this.secureCookie = secureCookie;
        this.sameSitePolicy = sameSitePolicy;
    }

    public HttpCookie createJWTCookie(final UserDetails userDetails) {
        final String jwt = tokenUtil.generateToken(userDetails);

        return ResponseCookie.from(JwtConstants.JWT_COOKIE_NAME, jwt)
                .maxAge(JwtConstants.JWT_TOKEN_VALIDITY_SECONDS)
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite(sameSitePolicy)
                .path("/")
                .build();
    }
}
