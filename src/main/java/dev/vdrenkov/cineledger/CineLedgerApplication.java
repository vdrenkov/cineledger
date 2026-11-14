package dev.vdrenkov.cineledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CineLedgerApplication {

    static void main(String[] args) {
        SpringApplication.run(CineLedgerApplication.class, args);
    }
}
