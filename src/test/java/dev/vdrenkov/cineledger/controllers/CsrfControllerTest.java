package dev.vdrenkov.cineledger.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsrfControllerTest {

    private final CsrfController csrfController = new CsrfController();

    @Test
    void csrf_returnsTokenPayload() {
        CsrfToken csrfToken = mock(CsrfToken.class);
        when(csrfToken.getToken()).thenReturn("csrf-token");

        Map<String, String> body = csrfController.csrf(csrfToken).getBody();

        assertEquals(Map.of("token", "csrf-token"), body);
    }
}

