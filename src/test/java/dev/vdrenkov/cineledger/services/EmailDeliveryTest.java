package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import dev.vdrenkov.cineledger.testutils.factories.OrderFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.event.TransactionalEventListenerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.math.BigDecimal;

class EmailDeliveryTest {
    private AnnotationConfigApplicationContext context;
    private MockRestServiceServer server;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        configure(true);
    }

    @AfterEach
    void tearDown() {
        TransactionSynchronizationManager.clear();
        context.close();
    }

    @Test
    void
            sendRegistrationConfirmationEmail_registeredCustomer_sendsPlainTextWithoutInventedDiscount() {
        server.expect(requestTo("https://mailjet.example.test/v3.1/send"))
                .andExpect(
                        jsonPath("$.Messages[0].Subject").value("Your registration confirmation"))
                .andExpect(
                        jsonPath("$.Messages[0].TextPart")
                                .value(
                                        org.hamcrest.Matchers.not(
                                                org.hamcrest.Matchers.containsString("5555"))))
                .andExpect(jsonPath("$.Messages[0].HTMLPart").doesNotExist())
                .andRespond(
                        withSuccess(
                                "{\"Messages\":[{\"Status\":\"success\"}]}",
                                MediaType.APPLICATION_JSON));

        emailService.sendRegistrationConfirmationEmail(UserFactory.getDefaultUser());

        server.verify();
    }

    @Test
    void sendPasswordResetEmail_validToken_sendsTokenWithoutPassword() {
        server.expect(requestTo("https://mailjet.example.test/v3.1/send"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Basic a2V5OnNlY3JldA=="))
                .andExpect(
                        jsonPath("$.Messages[0].TextPart")
                                .value(org.hamcrest.Matchers.containsString("one-time-token")))
                .andExpect(
                        jsonPath("$.Messages[0].To[0].Email")
                                .value(UserFactory.getDefaultUser().getEmail()))
                .andRespond(
                        withSuccess(
                                "{\"Messages\":[{\"Status\":\"success\"}]}",
                                MediaType.APPLICATION_JSON));

        emailService.sendPasswordResetEmail(UserFactory.getDefaultUser(), "one-time-token");

        server.verify();
    }

    @Test
    void sendOrderConfirmationEmail_decimalAmount_includesExactTotal() {
        final var order = OrderFactory.getDefaultOrder();
        order.setTotalPrice(new BigDecimal("11.70"));
        server.expect(requestTo("https://mailjet.example.test/v3.1/send"))
                .andExpect(
                        jsonPath("$.Messages[0].TextPart")
                                .value(org.hamcrest.Matchers.containsString("11.70 BGN")))
                .andRespond(
                        withSuccess(
                                "{\"Messages\":[{\"Status\":\"success\"}]}",
                                MediaType.APPLICATION_JSON));

        emailService.sendOrderConfirmationEmail(UserFactory.getDefaultUser(), order);

        server.verify();
    }

    @Test
    void sendPasswordResetEmail_transactionCommits_deliversOnlyAfterCommit() {
        server.expect(requestTo("https://mailjet.example.test/v3.1/send"))
                .andRespond(
                        withSuccess(
                                "{\"Messages\":[{\"Status\":\"success\"}]}",
                                MediaType.APPLICATION_JSON));
        TransactionSynchronizationManager.initSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(true);

        emailService.sendPasswordResetEmail(UserFactory.getDefaultUser(), "one-time-token");

        assertThrows(AssertionError.class, server::verify);
        TransactionSynchronizationUtils.triggerAfterCommit();
        TransactionSynchronizationUtils.triggerAfterCompletion(
                TransactionSynchronization.STATUS_COMMITTED);
        server.verify();
    }

    @Test
    void sendPasswordResetEmail_transactionRollsBack_doesNotDeliver() {
        TransactionSynchronizationManager.initSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(true);

        emailService.sendPasswordResetEmail(UserFactory.getDefaultUser(), "one-time-token");
        TransactionSynchronizationUtils.triggerAfterCompletion(
                TransactionSynchronization.STATUS_ROLLED_BACK);

        server.verify();
    }

    @Test
    void sendPasswordResetEmail_noCredentials_doesNotContactProvider() {
        context.close();
        configure(false);

        emailService.sendPasswordResetEmail(UserFactory.getDefaultUser(), "one-time-token");

        server.verify();
    }

    @Test
    void sendPasswordResetEmail_providerUnavailable_preservesCommittedOperation() {
        server.expect(requestTo("https://mailjet.example.test/v3.1/send"))
                .andRespond(withException(new IOException("Unavailable")));

        assertDoesNotThrow(() ->
                emailService.sendPasswordResetEmail(
                        UserFactory.getDefaultUser(), "one-time-token"));

        server.verify();
    }

    private void configure(final boolean enabled) {
        final RestClient.Builder builder =
                RestClient.builder()
                        .baseUrl("https://mailjet.example.test")
                        .defaultHeaders(headers -> headers.setBasicAuth("key", "secret"));
        server = MockRestServiceServer.bindTo(builder).build();
        context = new AnnotationConfigApplicationContext();
        context.registerBean(TransactionalEventListenerFactory.class);
        context.registerBean(
                EmailService.class,
                () ->
                        new EmailService(
                                builder.build(),
                                context,
                                enabled ? "key" : "",
                                enabled ? "secret" : "",
                                "sender@example.test",
                                "CineLedger"));
        context.refresh();
        emailService = context.getBean(EmailService.class);
    }
}
