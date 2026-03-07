package dev.vdrenkov.cineledger.configurations;

import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.repositories.RoleRepository;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static dev.vdrenkov.cineledger.utils.constants.RoleConstants.DEFAULT_ADMIN_ROLE;
import static dev.vdrenkov.cineledger.utils.constants.RoleConstants.DEFAULT_USER_ROLE;
import static dev.vdrenkov.cineledger.utils.constants.RoleConstants.DEFAULT_VENDOR_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleBootstrapRunnerTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void run_bootstrapDisabled_skipsAllBootstrapWork() {
        final RoleBootstrapRunner runner = new RoleBootstrapRunner(roleRepository, userRepository, passwordEncoder,
            bootstrapProperties(false, "", "", "", "", ""));

        runner.run();

        verify(roleRepository, never()).existsByName(any());
        verify(userRepository, never()).existsByUsername(any());
    }

    @Test
    void run_missingRoles_bootstrapsRequiredRoles() {
        RoleBootstrapRunner runner = new RoleBootstrapRunner(roleRepository, userRepository, passwordEncoder,
            bootstrapProperties(true, "", "", "", "", ""));
        when(roleRepository.existsByName(any())).thenReturn(false);

        runner.run();

        final ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository, times(3)).save(roleCaptor.capture());
        assertIterableEquals(List.of(DEFAULT_ADMIN_ROLE, DEFAULT_VENDOR_ROLE, DEFAULT_USER_ROLE),
            roleCaptor.getAllValues().stream().map(Role::getName).toList());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void run_adminConfigured_bootstrapsInitialAdminUser() {
        RoleBootstrapRunner runner = new RoleBootstrapRunner(roleRepository, userRepository, passwordEncoder,
            bootstrapProperties(true, "admin", "secret", "admin@cineledger.dev", "", ""));
        final Role adminRole = new Role(DEFAULT_ADMIN_ROLE);

        when(roleRepository.existsByName(any())).thenReturn(true);
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(roleRepository.findRoleByName(DEFAULT_ADMIN_ROLE)).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");

        runner.run();

        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        final User bootstrappedAdmin = userCaptor.getValue();
        assertEquals("admin", bootstrappedAdmin.getUsername());
        assertEquals("encoded-secret", bootstrappedAdmin.getPassword());
        assertEquals("admin@cineledger.dev", bootstrappedAdmin.getEmail());
        assertEquals("Admin", bootstrappedAdmin.getFirstName());
        assertEquals("User", bootstrappedAdmin.getLastName());
        assertEquals(List.of(adminRole), bootstrappedAdmin.getRoles());
    }

    private static BootstrapProperties bootstrapProperties(final boolean roles, final String username,
        final String password, final String email, final String firstName, final String lastName) {
        final BootstrapProperties bootstrapProperties = new BootstrapProperties();
        final BootstrapProperties.Admin admin = bootstrapProperties.getAdmin();

        bootstrapProperties.setRoles(roles);
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setEmail(email);
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        return bootstrapProperties;
    }
}

