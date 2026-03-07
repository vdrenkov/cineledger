package dev.vdrenkov.cineledger.configurations;

import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.repositories.RoleRepository;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import dev.vdrenkov.cineledger.utils.constants.RoleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Component
public class RoleBootstrapRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RoleBootstrapRunner.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean bootstrapRoles;
    private final String bootstrapAdminUsername;
    private final String bootstrapAdminPassword;
    private final String bootstrapAdminEmail;
    private final String bootstrapAdminFirstName;
    private final String bootstrapAdminLastName;

    public RoleBootstrapRunner(final RoleRepository roleRepository, final UserRepository userRepository,
        final PasswordEncoder passwordEncoder, @Value("${app.bootstrap.roles:true}") final boolean bootstrapRoles,
        @Value("${app.bootstrap.admin.username:}") final String bootstrapAdminUsername,
        @Value("${app.bootstrap.admin.password:}") final String bootstrapAdminPassword,
        @Value("${app.bootstrap.admin.email:}") final String bootstrapAdminEmail,
        @Value("${app.bootstrap.admin.first-name:}") final String bootstrapAdminFirstName,
        @Value("${app.bootstrap.admin.last-name:}") final String bootstrapAdminLastName) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bootstrapRoles = bootstrapRoles;
        this.bootstrapAdminUsername = bootstrapAdminUsername;
        this.bootstrapAdminPassword = bootstrapAdminPassword;
        this.bootstrapAdminEmail = bootstrapAdminEmail;
        this.bootstrapAdminFirstName = bootstrapAdminFirstName;
        this.bootstrapAdminLastName = bootstrapAdminLastName;
    }

    @Override
    @Transactional
    public void run(final String... args) {
        if (!bootstrapRoles) {
            log.info("Role bootstrap disabled by configuration");
            return;
        }

        ensureRoleExists(RoleConstants.DEFAULT_ADMIN_ROLE);
        ensureRoleExists(RoleConstants.DEFAULT_VENDOR_ROLE);
        ensureRoleExists(RoleConstants.DEFAULT_USER_ROLE);
        bootstrapAdminUserIfConfigured();
    }

    private void ensureRoleExists(final String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            roleRepository.save(new Role(roleName));
            log.info("Bootstrapped missing '{}' role", roleName);
        }
    }

    private void bootstrapAdminUserIfConfigured() {
        if (!StringUtils.hasText(bootstrapAdminUsername) || !StringUtils.hasText(bootstrapAdminPassword)
            || !StringUtils.hasText(bootstrapAdminEmail)) {
            return;
        }

        if (userRepository.existsByUsername(bootstrapAdminUsername)) {
            return;
        }

        final Role adminRole = roleRepository
            .findRoleByName(RoleConstants.DEFAULT_ADMIN_ROLE)
            .orElseThrow(() -> new IllegalStateException("ADMIN role is missing"));

        final User admin = new User(bootstrapAdminUsername, passwordEncoder.encode(bootstrapAdminPassword),
            bootstrapAdminEmail, StringUtils.hasText(bootstrapAdminFirstName) ? bootstrapAdminFirstName : "Admin",
            StringUtils.hasText(bootstrapAdminLastName) ? bootstrapAdminLastName : "User", LocalDate.now(),
            List.of(adminRole));

        userRepository.save(admin);
        log.info("Bootstrapped initial admin user '{}'", bootstrapAdminUsername);
    }
}
