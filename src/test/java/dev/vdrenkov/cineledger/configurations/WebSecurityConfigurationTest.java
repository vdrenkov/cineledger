package dev.vdrenkov.cineledger.configurations;

import dev.vdrenkov.cineledger.jwt.JwtRequestFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

/**
 * Tests web security configuration behavior.
 */
@ExtendWith(MockitoExtension.class)
class WebSecurityConfigurationTest {

    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private WebSecurityConfiguration webSecurityConfiguration;

    /**
     * Exercises the password encoder_ should return b crypt password encoder test scenario.
     */
    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        final PasswordEncoder passwordEncoder = webSecurityConfiguration.passwordEncoder();

        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    /**
     * Exercises the authentication manager_ should return configured authentication manager test scenario.
     */
    @Test
    void authenticationManager_ShouldReturnConfiguredAuthenticationManager() throws Exception {
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        final AuthenticationManager result = webSecurityConfiguration.authenticationManager(
            authenticationConfiguration);

        assertSame(authenticationManager, result);
    }
}



