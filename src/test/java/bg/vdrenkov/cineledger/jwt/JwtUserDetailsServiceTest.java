package bg.vdrenkov.cineledger.jwt;

import bg.vdrenkov.cineledger.exceptions.UserNotFoundException;
import bg.vdrenkov.cineledger.repositories.UserRepository;
import bg.vdrenkov.cineledger.testUtils.factories.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.PASSWORD;
import static bg.vdrenkov.cineledger.testUtils.constants.UserConstants.USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    public void testLoadUserByUsername_userFound_success() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(USERNAME);

        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(PASSWORD, userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    public void testLoadUserByUsername_userNotFound_throwsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> {

            when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

            jwtUserDetailsService.loadUserByUsername(USERNAME);

        });
    }
}



