package dev.vdrenkov.cineledger.services;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Sends transactional emails related to account lifecycle events and order processing. The service degrades gracefully
 * when Mailjet credentials are not configured.
 */
@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final String apiKey;
    private final String apiSecretKey;
    private final String senderEmail;
    private final String senderName;

    /**
     * Creates a new email service with the configured Mailjet credentials and sender identity.
     *
     * @param apiKey
     *     Mailjet API key used for authenticated requests.
     * @param apiSecretKey
     *     Mailjet API secret paired with the API key.
     * @param senderEmail
     *     Email address used in the message sender header.
     * @param senderName
     *     Human-readable sender name shown to recipients.
     */
    @Autowired
    public EmailService(@Value("${api.key}") String apiKey, @Value("${api.secret}") String apiSecretKey,
        @Value("${mail.sender.email}") String senderEmail, @Value("${mail.sender.name}") String senderName) {
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
    }

    /**
     * Sends an order confirmation email to the user who placed the order.
     *
     * @param user
     *     recipient of the email
     * @param orderDetails
     *     persisted order details included in the email body
     */
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

    /**
     * Sends a password recovery email containing the newly generated password.
     *
     * @param user
     *     recipient of the email
     * @param newPassword
     *     newly generated password communicated to the user
     */
    public void sendPasswordConfirmationEmail(User user, String newPassword) {
        final String recipientEmail = user.getEmail();
        final String recipientName = user.getFirstName() + " " + user.getLastName();

        final MailjetRequest request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES,
            new JSONArray().put(new JSONObject()
                .put(Emailv31.Message.FROM, new JSONObject().put("Email", senderEmail).put("Name", senderName))
                .put(Emailv31.Message.TO,
                    new JSONArray().put(new JSONObject().put("Email", recipientEmail).put("Name", recipientName)))
                .put(Emailv31.Message.SUBJECT, "Your password recovery confirmation")
                .put(Emailv31.Message.HTMLPART, "<h3>Dear " + recipientName
                    + ",</h3><p>You successfully changed your password. Here is your new password: <strong>"
                    + newPassword + "</strong></p>")));

        sendEmail(request, "password recovery");
    }

    /**
     * Sends a registration confirmation email after a successful user signup.
     *
     * @param user
     *     newly registered user
     */
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
            log.info("Skipping {} email because Mailjet credentials are not configured", emailType);
            return;
        }

        try {
            new MailjetClient(apiKey, apiSecretKey, new ClientOptions("v3.1")).post(request);
        } catch (MailjetSocketTimeoutException | MailjetException exception) {
            log.warn("Could not send {} email", emailType, exception);
        }
    }
}
