package dev.vdrenkov.cineledger.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds the startup bootstrap configuration used for role seeding and optional admin creation.
 */
@Getter
@ConfigurationProperties(prefix = "app.bootstrap")
public class BootstrapProperties {
    private final Admin admin = new Admin();
    @Setter
    private boolean roles = true;

    /**
     * Holds the optional initial admin credentials used during bootstrap.
     */
    @Setter
    @Getter
    public static class Admin {
        private String username = "";
        private String password = "";
        private String email = "";
        private String firstName = "";
        private String lastName = "";
    }
}
