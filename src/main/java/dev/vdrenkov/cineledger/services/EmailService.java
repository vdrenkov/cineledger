package dev.vdrenkov.cineledger.services;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final String apiKey;
    private final String apiSecretKey;
    private final String senderEmail;
    private final String senderName;

    public EmailService(@Value("${api.key}") String apiKey, @Value("${api.secret}") String apiSecretKey,
        @Value("${mail.sender.email}") String senderEmail, @Value("${mail.sender.name}") String senderName) {
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
    }

    public void sendOrderConfirmationEmail(User user, Order orderDetails) {
        final String recipientEmail = user.getEmail();
        final String recipientName = user.getFirstName() + " " + user.getLastName();

        final MailjetRequest request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES,
            new JSONArray().put(new JSONObject()
                .put(Emailv31.Message.FROM, new JSONObject().put("Email", senderEmail).put("Name", senderName))
                .put(Emailv31.Message.TO,
                    new JSONArray().put(new JSONObject().put("Email", recipientEmail).put("Name", recipientName)))
                .put(Emailv31.Message.SUBJECT, "Your order confirmation")
                .put(Emailv31.Message.HTMLPART,
                    "<h3>Dear " + recipientName + ",</h3><p>You successfully created the order number: <strong>"
                        + orderDetails.getId() + "</strong>. The total price of the order is: <strong>"
                        + orderDetails.getTotalPrice() + " lv.</strong></p>")));

        sendEmail(request, "order confirmation");
    }

    public void sendPasswordResetEmail(User user, String token) {
        final String recipientEmail = user.getEmail();
        final String recipientName = user.getFirstName() + " " + user.getLastName();

        final MailjetRequest request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES,
            new JSONArray().put(new JSONObject()
                .put(Emailv31.Message.FROM, new JSONObject().put("Email", senderEmail).put("Name", senderName))
                .put(Emailv31.Message.TO,
                    new JSONArray().put(new JSONObject().put("Email", recipientEmail).put("Name", recipientName)))
                .put(Emailv31.Message.SUBJECT, "Reset your CineLedger password")
                .put(Emailv31.Message.HTMLPART, "<h3>Dear " + recipientName
                    + ",</h3><p>Submit this one-time token to POST /password-resets within 30 minutes: <strong>"
                    + token + "</strong>. If you did not request this, ignore this message.</p>")));

        sendEmail(request, "password recovery");
    }

    public void sendRegistrationConfirmationEmail(User user) {
        final String recipientEmail = user.getEmail();
        final String recipientName = user.getFirstName() + " " + user.getLastName();

        final String discountMessage = "As our user here is your discount code for online reservation: 5555";

        final MailjetRequest request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES,
            new JSONArray().put(new JSONObject()
                .put(Emailv31.Message.FROM, new JSONObject().put("Email", senderEmail).put("Name", senderName))
                .put(Emailv31.Message.TO,
                    new JSONArray().put(new JSONObject().put("Email", recipientEmail).put("Name", recipientName)))
                .put(Emailv31.Message.SUBJECT, "Your registration confirmation")
                .put(Emailv31.Message.HTMLPART,
                    "<h3>Dear " + recipientName + ",</h3><p>You have successfully registered. " + discountMessage
                        + "</p>")));

        sendEmail(request, "registration confirmation");
    }

    private void sendEmail(final MailjetRequest request, final String emailType) {
        if (!StringUtils.hasText(apiKey) || !StringUtils.hasText(apiSecretKey)) {

            return;
        }

        try {
            final var options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecretKey)
                .build();
            new MailjetClient(options).post(request);
        } catch (final MailjetException exception) {
            log.warn("Could not send {} email", emailType, exception);
        }
    }
}
