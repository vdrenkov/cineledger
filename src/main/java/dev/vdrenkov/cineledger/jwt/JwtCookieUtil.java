package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.utils.constants.JwtConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Builds HTTP cookies that carry JWT authentication tokens.
 */
@Component
public class JwtCookieUtil {

    private final Logger log = LoggerFactory.getLogger(JwtCookieUtil.class);

    private final JwtTokenUtil tokenUtil;
    private final boolean secureCookie;
    private final String sameSitePolicy;

    /**
     * Creates a new jwt cookie util with its required collaborators.
     *
     * @param tokenUtil
     *     token util used by the operation
     * @param secureCookie
     *     secure cookie used by the operation
     * @param sameSitePolicy
     *     same site policy used by the operation
     */
    @Autowired
    public JwtCookieUtil(final JwtTokenUtil tokenUtil, @Value("${jwt.cookie.secure:true}") final boolean secureCookie,
        @Value("${jwt.cookie.same-site:Lax}") final String sameSitePolicy) {
        this.tokenUtil = tokenUtil;
        this.secureCookie = secureCookie;
        this.sameSitePolicy = sameSitePolicy;
    }

    /**
     * Creates an HTTP-only JWT cookie for the authenticated user.
     *
     * @param userDetails
     *     authenticated user details
     * @return generated JWT cookie
     */
    public HttpCookie createJWTCookie(final UserDetails userDetails) {
        final String jwt = tokenUtil.generateToken(userDetails);

        log.info("An attempt to create a JWT cookie");
        return ResponseCookie
            .from(JwtConstants.JWT_COOKIE_NAME, jwt)
            .maxAge(JwtConstants.JWT_TOKEN_VALIDITY_SECONDS)
            .httpOnly(true)
            .secure(secureCookie)
            .sameSite(sameSitePolicy)
            .path("/")
            .build();
    }
}

