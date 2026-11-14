package dev.vdrenkov.cineledger.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Clock;
import java.time.Duration;

@Configuration
public class WebConfiguration {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public RestClient mailjetClient(
            @Value("${api.key}") final String apiKey,
            @Value("${api.secret}") final String apiSecret,
            @Value("${mail.url}") final String baseUrl) {
        final var httpClient =
                HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
        final var factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofSeconds(5));
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(apiKey, apiSecret))
                .requestFactory(factory)
                .build();
    }
}
