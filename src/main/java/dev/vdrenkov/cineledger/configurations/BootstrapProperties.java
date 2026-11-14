package dev.vdrenkov.cineledger.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.bootstrap")
public record BootstrapProperties(@DefaultValue("true") boolean roles, @DefaultValue Admin admin) {
    public record Admin(@DefaultValue("") String username, @DefaultValue("") String password,
                        @DefaultValue("") String email, @DefaultValue("") String firstName,
                        @DefaultValue("") String lastName) {}
}
