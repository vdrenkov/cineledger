package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.testutils.factories.JwtFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;

import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.JWT_COOKIE_NAME;
import static dev.vdrenkov.cineledger.testutils.constants.JwtConstants.JWT_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests jwt cookie util behavior.
 */
@ExtendWith(MockitoExtension.class)
class JwtCookieUtilTest {

    private static final boolean COOKIE_SECURE = false;
    private static final String SAME_SITE = "Lax";

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Verifies that create J W T Cookie cookie Created success.
     */
    @Test
    void testCreateJWTCookie_cookieCreated_success() {
        final JwtCookieUtil jwtCookieUtil = new JwtCookieUtil(jwtTokenUtil, COOKIE_SECURE, SAME_SITE);
        when(jwtTokenUtil.generateToken(any())).thenReturn(JWT_TOKEN);

        final HttpCookie httpCookie = jwtCookieUtil.createJWTCookie(JwtFactory.getDefaultUserDetails());
        final ResponseCookie responseCookie = (ResponseCookie) httpCookie;

        assertEquals(JWT_COOKIE_NAME, httpCookie.getName());
        assertEquals(JWT_TOKEN, httpCookie.getValue());
        assertEquals(3600, responseCookie.getMaxAge().toSeconds());
    }
}



