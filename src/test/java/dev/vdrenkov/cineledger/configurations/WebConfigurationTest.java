package dev.vdrenkov.cineledger.configurations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class WebConfigurationTest {

    @Spy
    private WebConfiguration webConfiguration;

    @Test
    void testRestTemplate() {
        final RestTemplate restTemplate = webConfiguration.restTemplate();

        assertNotNull(restTemplate);
    }
}
