package dev.vdrenkov.cineledger.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Exposes REST endpoints for managing csrf data.
 */
@RestController
public class CsrfController {

    /**
     * Executes the csrf operation for csrf.
     *
     * @param csrfToken
     *     csrf token used by the operation
     * @return HTTP response describing the operation result
     */
    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> csrf(final CsrfToken csrfToken) {
        return ResponseEntity.ok(Map.of("token", csrfToken.getToken()));
    }
}
