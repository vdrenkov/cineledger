package dev.vdrenkov.cineledger.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.Map;

class CsrfControllerTest {
    private final CsrfController csrfController = new CsrfController();

    @Test
    void csrf_returnsTokenPayload() {
        final CsrfToken csrfToken = mock(CsrfToken.class);
        when(csrfToken.getToken()).thenReturn("csrf-token");

        final Map<String, String> body = csrfController.csrf(csrfToken).getBody();

        assertEquals(Map.of("token", "csrf-token"), body);
    }
}
