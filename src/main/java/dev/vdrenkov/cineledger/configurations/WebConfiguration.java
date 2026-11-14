package dev.vdrenkov.cineledger.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {

    @Bean
    public java.time.Clock clock() {
        return java.time.Clock.systemUTC();
    }

}
