package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final RestClient mailjetClient;
    private final ApplicationEventPublisher events;
    private final boolean enabled;
    private final String senderEmail;
    private final String senderName;

    public EmailService(
            final RestClient mailjetClient,
            final ApplicationEventPublisher events,
            @Value("${api.key}") final String apiKey,
            @Value("${api.secret}") final String apiSecret,
            @Value("${mail.sender.email}") final String senderEmail,
            @Value("${mail.sender.name}") final String senderName) {
        this.mailjetClient = mailjetClient;
        this.events = events;
        this.enabled = StringUtils.hasText(apiKey) && StringUtils.hasText(apiSecret);
        this.senderEmail = senderEmail;
        this.senderName = senderName;
    }

    public void sendRegistrationConfirmationEmail(final User user) {
        publish(
                user,
                "Your registration confirmation",
                "Your CineLedger account has been created.");
    }

    public void sendOrderConfirmationEmail(final User user, final Order order) {
        publish(
                user,
                "Your order confirmation",
                "Your order number is "
                        + order.getId()
                        + ". The total is "
                        + order.getTotalPrice().toPlainString()
                        + " BGN.");
    }

    public void sendPasswordResetEmail(final User user, final String token) {
        publish(
                user,
                "Reset your CineLedger password",
                "Submit this one-time token to POST /password-resets within 30 minutes: "
                        + token
                        + ". If you did not request this, ignore this message.");
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void deliver(final Notification notification) {

        if (!enabled) {
            return;
        }

        final var message =
                Map.of(
                        "From",
                        Map.of("Email", senderEmail, "Name", senderName),
                        "To",
                        List.of(Map.of("Email", notification.email(), "Name", notification.name())),
                        "Subject",
                        notification.subject(),
                        "TextPart",
                        notification.text());
        try {
            final JsonNode response =
                    mailjetClient
                            .post()
                            .uri("/v3.1/send")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Map.of("Messages", List.of(message)))
                            .retrieve()
                            .body(JsonNode.class);

            if (response == null
                    || !"success"
                            .equals(response.path("Messages").path(0).path("Status").asString())) {
                log.warn("Mailjet did not accept the notification");
            }

        } catch (final RestClientException exception) {
            log.warn("Mailjet notification delivery failed");
        }
    }

    private void publish(final User user, final String subject, final String text) {
        final String name = user.getFirstName() + " " + user.getLastName();
        events.publishEvent(
                new Notification(user.getEmail(), name, subject, "Dear " + name + ",\n\n" + text));
    }

    public record Notification(String email, String name, String subject, String text) {}
}
