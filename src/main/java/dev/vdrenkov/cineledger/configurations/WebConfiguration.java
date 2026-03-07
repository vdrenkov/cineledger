package dev.vdrenkov.cineledger.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Defines shared web-layer infrastructure beans.
 */
@Configuration
public class WebConfiguration {
    /**
     * Creates the shared RestTemplate bean used for outbound HTTP calls.
     *
     * @return requested rest template value
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


