package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.utils.constants.JwtConstants;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * Restores Spring Security authentication from the JWT cookie on each request.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final JwtTokenUtil tokenUtil;
    private final JwtUserDetailsService userDetailsService;

    /**
     * Creates a new jwt request filter with its required collaborators.
     *
     * @param tokenUtil
     *     token util used by the operation
     * @param userDetailsService
     *     user details service used by the operation
     */
    @Autowired
    public JwtRequestFilter(final JwtTokenUtil tokenUtil, final JwtUserDetailsService userDetailsService) {
        this.tokenUtil = tokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Restores authentication from the JWT cookie when a valid token is present.
     *
     * @param request
     *     request payload containing the submitted data
     * @param response
     *     current HTTP response
     * @param chain
     *     remaining servlet filter chain
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final @NonNull HttpServletResponse response,
        final @NonNull FilterChain chain) throws ServletException, IOException {
        final String token = getJwtToken(request.getCookies());
        String username = null;

        if (token != null && !token.isEmpty()) {
            log.info("JWT found");
            try {
                username = tokenUtil.getUsernameFromToken(token);
            } catch (JwtException | IllegalArgumentException exception) {
                log.warn("Invalid JWT provided in cookie", exception);
            }
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null && username != null) {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (tokenUtil.validateToken(token, userDetails)) {
                final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(request);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("Authentication set");
            }
        }
        chain.doFilter(request, response);
    }

    private String getJwtToken(final Cookie[] cookies) {
        log.info("An attempt to retrieve a JWT cookie from all cookies");
        if (cookies != null) {
            return Arrays
                .stream(cookies)
                .filter(cookie -> JwtConstants.JWT_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        }
        return null;
    }
}


