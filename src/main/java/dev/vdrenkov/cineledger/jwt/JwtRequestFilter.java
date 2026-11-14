package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.exceptions.UserNotFoundException;
import dev.vdrenkov.cineledger.utils.constants.JwtConstants;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil tokenUtil;
    private final JwtUserDetailsService userDetailsService;

    public JwtRequestFilter(
            final JwtTokenUtil tokenUtil, final JwtUserDetailsService userDetailsService) {
        this.tokenUtil = tokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final @NonNull HttpServletResponse response,
            final @NonNull FilterChain chain)
            throws ServletException, IOException {
        final String token = getJwtToken(request.getCookies());

        if (token != null
                && !token.isBlank()
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                final var claims = tokenUtil.parseToken(token);
                final var userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
                if (userDetails instanceof AuthenticatedUser user
                        && claims.get("version", Long.class) == user.credentialsVersion()) {
                    final var authentication =
                            UsernamePasswordAuthenticationToken.authenticated(
                                    userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (final JwtException
                    | IllegalArgumentException
                    | UsernameNotFoundException
                    | UserNotFoundException exception) {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }

    private static String getJwtToken(final Cookie[] cookies) {

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> JwtConstants.JWT_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
