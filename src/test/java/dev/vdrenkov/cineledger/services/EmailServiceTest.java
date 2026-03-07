package dev.vdrenkov.cineledger.services;

import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.testutils.constants.UserConstants;
import dev.vdrenkov.cineledger.testutils.factories.OrderFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Tests email service behavior.
 */
@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private MailjetResponse mailjetResponse;

    @InjectMocks
    private EmailService emailService;

    /**
     * Verifies that send Order Confirmation Email.
     */
    @Test
    void testSendOrderConfirmationEmail() throws JSONException, MailjetSocketTimeoutException, MailjetException {
        final User user = UserFactory.getDefaultUser();
        final Order order = OrderFactory.getDefaultOrder();
        final JSONObject data = new JSONObject();
        when(mailjetResponse.getStatus()).thenReturn(200);
        data.put("Email", user.getEmail());
        when(mailjetResponse.getData()).thenReturn(new JSONArray().put(data));

        emailService.sendOrderConfirmationEmail(user, order);

        assertEquals(200, mailjetResponse.getStatus());
        assertEquals(user.getEmail(), mailjetResponse.getData().getJSONObject(0).getString("Email"));
    }

    /**
     * Verifies that send Password Confirmation Email.
     */
    @Test
    void testSendPasswordConfirmationEmail() throws JSONException, MailjetSocketTimeoutException, MailjetException {
        final User user = UserFactory.getDefaultUser();
        final JSONObject data = new JSONObject();
        when(mailjetResponse.getStatus()).thenReturn(200);
        data.put("Email", user.getEmail());
        when(mailjetResponse.getData()).thenReturn(new JSONArray().put(data));

        emailService.sendPasswordConfirmationEmail(user, UserConstants.PASSWORD);

        assertEquals(200, mailjetResponse.getStatus());
        assertEquals(user.getEmail(), mailjetResponse.getData().getJSONObject(0).getString("Email"));
    }

    /**
     * Verifies that send Registration Confirmation Email.
     */
    @Test
    void testSendRegistrationConfirmationEmail() throws JSONException, MailjetSocketTimeoutException, MailjetException {

        final User user = UserFactory.getDefaultUser();

        final JSONObject data = new JSONObject();
        when(mailjetResponse.getStatus()).thenReturn(200);
        data.put("Email", user.getEmail());
        when(mailjetResponse.getData()).thenReturn(new JSONArray().put(data));

        emailService.sendRegistrationConfirmationEmail(user);

        assertEquals(200, mailjetResponse.getStatus());
        assertEquals(user.getEmail(), mailjetResponse.getData().getJSONObject(0).getString("Email"));
    }
}




