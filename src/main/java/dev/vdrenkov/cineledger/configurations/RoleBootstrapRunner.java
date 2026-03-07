package dev.vdrenkov.cineledger.configurations;

import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.repositories.RoleRepository;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import dev.vdrenkov.cineledger.utils.constants.RoleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Seeds required roles and an optional bootstrap administrator during application startup.
 */
@Component
public class RoleBootstrapRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RoleBootstrapRunner.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BootstrapProperties bootstrapProperties;

    /**
     * Creates a new role bootstrap runner with its required collaborators.
     *
     * @param roleRepository
     *     role repository used by the operation
     * @param userRepository
     *     user repository used by the operation
     * @param passwordEncoder
     *     password encoder used by the operation
     * @param bootstrapProperties
     *     bootstrap properties used by the operation
     */
    public RoleBootstrapRunner(final RoleRepository roleRepository, final UserRepository userRepository,
        final PasswordEncoder passwordEncoder, final BootstrapProperties bootstrapProperties) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bootstrapProperties = bootstrapProperties;
    }

    /**
     * Bootstraps required roles and an optional administrator account at startup.
     *
     * @param args
     *     application startup arguments
     */
    @Override
    @Transactional
    public void run(final String... args) {
        if (!bootstrapProperties.isRoles()) {
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
        final BootstrapProperties.Admin admin = bootstrapProperties.getAdmin();

        if (!StringUtils.hasText(admin.getUsername()) || !StringUtils.hasText(admin.getPassword())
            || !StringUtils.hasText(admin.getEmail())) {
            return;
        }

        if (userRepository.existsByUsername(admin.getUsername())) {
            return;
        }

        final Role adminRole = roleRepository
            .findRoleByName(RoleConstants.DEFAULT_ADMIN_ROLE)
            .orElseThrow(() -> new IllegalStateException("ADMIN role is missing"));

        final User bootstrappedAdmin = new User(admin.getUsername(), passwordEncoder.encode(admin.getPassword()),
            admin.getEmail(), StringUtils.hasText(admin.getFirstName()) ? admin.getFirstName() : "Admin",
            StringUtils.hasText(admin.getLastName()) ? admin.getLastName() : "User", LocalDate.now(),
            List.of(adminRole));

        userRepository.save(bootstrappedAdmin);
        log.info("Bootstrapped initial admin user '{}'", admin.getUsername());
    }
}
