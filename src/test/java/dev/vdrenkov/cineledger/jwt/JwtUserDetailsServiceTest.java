package dev.vdrenkov.cineledger.jwt;

import dev.vdrenkov.cineledger.exceptions.UserNotFoundException;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.PASSWORD;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests jwt user details service behavior.
 */
@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    /**
     * Verifies that load User By Username user Found success.
     */
    @Test
    void testLoadUserByUsername_userFound_success() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(USERNAME);

        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(PASSWORD, userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
    }

    /**
     * Verifies that load User By Username user Not Found throws User Not Found Exception.
     */
    @Test
    void testLoadUserByUsername_userNotFound_throwsUserNotFoundException() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> jwtUserDetailsService.loadUserByUsername(USERNAME));
    }
}



