package bg.vdrenkov.cineledger.configurations;

import bg.vdrenkov.cineledger.jwt.JwtRequestFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebSecurityConfigurationTest {

    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private WebSecurityConfiguration webSecurityConfiguration;

    @Test
    public void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        final PasswordEncoder passwordEncoder = webSecurityConfiguration.passwordEncoder();

        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void authenticationManager_ShouldReturnConfiguredAuthenticationManager() throws Exception {
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        final AuthenticationManager result = webSecurityConfiguration.authenticationManager(
            authenticationConfiguration);

        assertSame(authenticationManager, result);
    }
}



