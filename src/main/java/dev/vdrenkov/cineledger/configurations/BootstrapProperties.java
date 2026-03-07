package dev.vdrenkov.cineledger.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds the startup bootstrap configuration used for role seeding and optional admin creation.
 */
@ConfigurationProperties(prefix = "app.bootstrap")
public class BootstrapProperties {

    private final Admin admin = new Admin();
    private boolean roles = true;

    /**
     * Returns whether startup role bootstrap is enabled.
     *
     * @return {@code true} when bootstrap should run, otherwise {@code false}
     */
    public boolean isRoles() {
        return roles;
    }

    /**
     * Updates whether startup role bootstrap is enabled.
     *
     * @param roles
     *     whether bootstrap should run
     */
    public void setRoles(final boolean roles) {
        this.roles = roles;
    }

    /**
     * Returns the optional bootstrap admin configuration.
     *
     * @return bootstrap admin settings
     */
    public Admin getAdmin() {
        return admin;
    }

    /**
     * Holds the optional initial admin credentials used during bootstrap.
     */
    public static class Admin {

        private String username = "";
        private String password = "";
        private String email = "";
        private String firstName = "";
        private String lastName = "";

        /**
         * Returns the bootstrap admin username.
         *
         * @return configured username
         */
        public String getUsername() {
            return username;
        }

        /**
         * Updates the bootstrap admin username.
         *
         * @param username
         *     configured username
         */
        public void setUsername(final String username) {
            this.username = username;
        }

        /**
         * Returns the bootstrap admin password.
         *
         * @return configured password
         */
        public String getPassword() {
            return password;
        }

        /**
         * Updates the bootstrap admin password.
         *
         * @param password
         *     configured password
         */
        public void setPassword(final String password) {
            this.password = password;
        }

        /**
         * Returns the bootstrap admin email address.
         *
         * @return configured email address
         */
        public String getEmail() {
            return email;
        }

        /**
         * Updates the bootstrap admin email address.
         *
         * @param email
         *     configured email address
         */
        public void setEmail(final String email) {
            this.email = email;
        }

        /**
         * Returns the bootstrap admin first name.
         *
         * @return configured first name
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * Updates the bootstrap admin first name.
         *
         * @param firstName
         *     configured first name
         */
        public void setFirstName(final String firstName) {
            this.firstName = firstName;
        }

        /**
         * Returns the bootstrap admin last name.
         *
         * @return configured last name
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * Updates the bootstrap admin last name.
         *
         * @param lastName
         *     configured last name
         */
        public void setLastName(final String lastName) {
            this.lastName = lastName;
        }
    }
}
