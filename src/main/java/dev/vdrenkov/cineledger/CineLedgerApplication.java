package dev.vdrenkov.cineledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Starts the CineLedger Spring Boot application and enables configuration properties binding.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class CineLedgerApplication {

    /**
     * Boots the application with the provided startup arguments.
     *
     * @param args
     *     application startup arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CineLedgerApplication.class, args);
    }
}

