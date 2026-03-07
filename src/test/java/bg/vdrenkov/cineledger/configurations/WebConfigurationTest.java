package bg.vdrenkov.cineledger.configurations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class WebConfigurationTest {

    @Spy
    private WebConfiguration webConfiguration;

    @Test
    public void testRestTemplate() {
        RestTemplate restTemplate = webConfiguration.restTemplate();

        assertNotNull(restTemplate);
    }

    @Test
    public void testRandom() {
        Random random = webConfiguration.random();

        assertNotNull(random);
        assertTrue(random instanceof SecureRandom);
    }
}




