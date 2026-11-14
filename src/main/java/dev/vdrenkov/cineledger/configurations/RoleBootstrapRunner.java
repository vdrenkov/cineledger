package dev.vdrenkov.cineledger.configurations;

import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.repositories.RoleRepository;
import dev.vdrenkov.cineledger.repositories.UserRepository;
import dev.vdrenkov.cineledger.utils.constants.RoleConstants;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Component
public class RoleBootstrapRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BootstrapProperties bootstrapProperties;

    public RoleBootstrapRunner(
            final RoleRepository roleRepository,
            final UserRepository userRepository,
            final PasswordEncoder passwordEncoder,
            final BootstrapProperties bootstrapProperties) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bootstrapProperties = bootstrapProperties;
    }

    @Override
    @Transactional
    public void run(final String @NonNull ... args) {
        if (!bootstrapProperties.roles()) {
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
        }
    }

    private void bootstrapAdminUserIfConfigured() {
        final BootstrapProperties.Admin admin = bootstrapProperties.admin();

        if (!StringUtils.hasText(admin.username())
                || !StringUtils.hasText(admin.password())
                || !StringUtils.hasText(admin.email())) {
            return;
        }

        if (userRepository.existsByUsername(admin.username())) {
            return;
        }

        final Role adminRole =
                roleRepository
                        .findRoleByName(RoleConstants.DEFAULT_ADMIN_ROLE)
                        .orElseThrow(() -> new IllegalStateException("ADMIN role is missing"));

        final User bootstrappedAdmin =
                new User(
                        admin.username(),
                        passwordEncoder.encode(admin.password()),
                        admin.email(),
                        StringUtils.hasText(admin.firstName()) ? admin.firstName() : "Admin",
                        StringUtils.hasText(admin.lastName()) ? admin.lastName() : "User",
                        LocalDate.now(),
                        List.of(adminRole));

        userRepository.save(bootstrappedAdmin);
    }
}
