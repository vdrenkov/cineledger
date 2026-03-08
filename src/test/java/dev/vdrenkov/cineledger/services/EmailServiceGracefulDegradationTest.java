package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.testutils.factories.OrderFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailServiceGracefulDegradationTest {
    private final EmailService emailService = new EmailService("", "", "no-reply@cineledger.dev", "CineLedger");

    @Test
    void sendRegistrationConfirmationEmail_withoutMailjetCredentials_doesNotFail() {
        assertDoesNotThrow(() -> emailService.sendRegistrationConfirmationEmail(UserFactory.getDefaultUser()));
    }

    @Test
    void sendPasswordConfirmationEmail_withoutMailjetCredentials_doesNotFail() {
        assertDoesNotThrow(
            () -> emailService.sendPasswordConfirmationEmail(UserFactory.getDefaultUser(), "new-password"));
    }

    @Test
    void sendOrderConfirmationEmail_withoutMailjetCredentials_doesNotFail() {
        assertDoesNotThrow(() -> emailService.sendOrderConfirmationEmail(UserFactory.getDefaultUser(),
            OrderFactory.getDefaultOrder()));
    }
}

