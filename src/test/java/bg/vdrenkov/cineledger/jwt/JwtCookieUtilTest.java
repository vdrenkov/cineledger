package bg.vdrenkov.cineledger.jwt;

import bg.vdrenkov.cineledger.testUtils.factories.JwtFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;

import static bg.vdrenkov.cineledger.testUtils.constants.JwtConstants.JWT_COOKIE_NAME;
import static bg.vdrenkov.cineledger.testUtils.constants.JwtConstants.JWT_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtCookieUtilTest {

  private static final boolean COOKIE_SECURE = false;
  private static final String SAME_SITE = "Lax";

  @Mock
  private JwtTokenUtil jwtTokenUtil;

  @Test
  public void testCreateJWTCookie_cookieCreated_success() {
    JwtCookieUtil jwtCookieUtil = new JwtCookieUtil(jwtTokenUtil, COOKIE_SECURE, SAME_SITE);
    when(jwtTokenUtil.generateToken(any())).thenReturn(JWT_TOKEN);

    HttpCookie httpCookie = jwtCookieUtil.createJWTCookie(JwtFactory.getDefaultUserDetails());
    ResponseCookie responseCookie = (ResponseCookie) httpCookie;

    assertEquals(JWT_COOKIE_NAME, httpCookie.getName());
    assertEquals(JWT_TOKEN, httpCookie.getValue());
    assertEquals(3600, responseCookie.getMaxAge().toSeconds());
  }
}



