package bg.vdrenkov.cineledger.jwt;

import bg.vdrenkov.cineledger.testUtils.factories.JwtFactory;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static bg.vdrenkov.cineledger.testUtils.constants.JwtConstants.JWT_USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class JwtRequestFilterTest {

  private static final String SECRET = "test-secret-for-jwt-tests-should-be-at-least-thirty-two-characters";

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
  public void setUp() {
    JwtFactory.setSecret(SECRET);
  }

  @Test
  public void testDoFilterInternal_success() throws ServletException, IOException {
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
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

  @Test
  public void testDoFilterInternal_cookiesAreNull() throws ServletException, IOException {
    when(request.getCookies()).thenReturn(null);

    requestFilter.doFilterInternal(request, response, filterChain);

    verify(request, times(1)).getCookies();
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  public void testDoFilterInternal_invalidJwt_doesNotAuthenticate() throws ServletException, IOException {
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(null);
    when(request.getCookies()).thenReturn(JwtFactory.getDefaultCookieArray());
    when(tokenUtil.getUsernameFromToken(anyString())).thenThrow(new JwtException("invalid token"));

    requestFilter.doFilterInternal(request, response, filterChain);

    verifyNoInteractions(userDetailsService);
    verify(filterChain, times(1)).doFilter(request, response);
  }
}



