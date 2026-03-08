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

/**
 * Tests jwt request filter behavior.
 */
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

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setUp() {
        JwtFactory.setSecret(SECRET);
    }

    /**
     * Verifies that do Filter Internal success.
     */
    @Test
    void testDoFilterInternal_success() throws ServletException, IOException {
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(request.getCookies()).thenReturn(JwtFactory.getDefaultCookieArray());
        when(tokenUtil.getUsernameFromToken(anyString())).thenReturn(JWT_USERNAME);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(JwtFactory.getDefaultUserDetails());
        when(tokenUtil.validateToken(anyString(), any())).thenReturn(true);

        requestFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, times(1)).loadUserByUsername(JWT_USERNAME);
        verify(tokenUtil, times(1)).validateToken(any(), any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Verifies that do Filter Internal cookies Are Null.
     */
    @Test
    void testDoFilterInternal_cookiesAreNull() throws ServletException, IOException {
        when(request.getCookies()).thenReturn(null);

        requestFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getCookies();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Verifies that do Filter Internal invalid Jwt does Not Authenticate.
     */
    @Test
    void testDoFilterInternal_invalidJwt_doesNotAuthenticate() throws ServletException, IOException {
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(request.getCookies()).thenReturn(JwtFactory.getDefaultCookieArray());
        when(tokenUtil.getUsernameFromToken(anyString())).thenThrow(new JwtException("invalid token"));

        requestFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(userDetailsService);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}



