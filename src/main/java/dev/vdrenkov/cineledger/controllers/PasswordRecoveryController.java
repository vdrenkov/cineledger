package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.PasswordRecoveryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordRecoveryController {
    private final PasswordRecoveryService recoveryService;

    public PasswordRecoveryController(final PasswordRecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    @PostMapping("/password-reset-requests")
    public ResponseEntity<Void> requestReset(@RequestBody @Valid final ResetRequest request) {
        recoveryService.requestReset(request.username());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/password-resets")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid final ResetConfirmation request) {
        recoveryService.resetPassword(request.token(), request.password());
        return ResponseEntity.noContent().build();
    }

    public record ResetRequest(@NotBlank @Size(max = 100) String username) {}

    public record ResetConfirmation(
            @NotBlank @Pattern(regexp = "[A-Za-z0-9_-]{43}") String token,
            @NotBlank @Size(min = 8, max = 72) String password) {}
}
