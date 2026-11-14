package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.testutils.factories.JwtFactory;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.JWT_USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {
    private static final String SECRET = "xJ4nRq8sUv2mKe7pTd5yLa9cBh3wNz6fQg1rMs8vPk4tYd7uCe2hWn5qLb9xRa3mF";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private JwtTokenUtil tokenUtil;

    @Mock
    private JwtUserDetailsService userDetailsService;

    @InjectMocks
    private JwtRequestFilter requestFilter;

    @BeforeEach
    void setUp() {
        JwtFactory.setSecret(SECRET);
    }

    @Test
    void testDoFilterInternal_success() throws ServletException, IOException {
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(request.getCookies()).thenReturn(JwtFactory.getDefaultCookieArray());
        when(tokenUtil.parseToken(anyString())).thenReturn(new JwtTokenUtil(SECRET).parseToken(JwtFactory.getDefaultJwtToken()));
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(JwtFactory.getDefaultUserDetails());

        requestFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, times(1)).loadUserByUsername(JWT_USERNAME);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_cookiesAreNull() throws ServletException, IOException {
        when(request.getCookies()).thenReturn(null);

        requestFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getCookies();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_invalidJwt_doesNotAuthenticate() throws ServletException, IOException {
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(request.getCookies()).thenReturn(JwtFactory.getDefaultCookieArray());
        when(tokenUtil.parseToken(anyString())).thenThrow(new JwtException("invalid token"));

        requestFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(userDetailsService);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
