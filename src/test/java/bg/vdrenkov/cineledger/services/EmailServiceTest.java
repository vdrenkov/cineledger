package bg.vdrenkov.cineledger.services;

import org.junit.jupiter.api.extension.ExtendWith;
import bg.vdrenkov.cineledger.models.entities.Order;
import bg.vdrenkov.cineledger.models.entities.User;
import bg.vdrenkov.cineledger.testUtils.constants.UserConstants;
import bg.vdrenkov.cineledger.testUtils.factories.OrderFactory;
import bg.vdrenkov.cineledger.testUtils.factories.UserFactory;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

  @Mock
  private MailjetResponse mailjetResponse;

  @InjectMocks
  private EmailService emailService;

  @Test
  public void testSendOrderConfirmationEmail() throws JSONException, MailjetSocketTimeoutException, MailjetException {
    User user = UserFactory.getDefaultUser();
    Order order = OrderFactory.getDefaultOrder();
    JSONObject data = new JSONObject();
    when(mailjetResponse.getStatus()).thenReturn(200);
    data.put("Email", user.getEmail());
    when(mailjetResponse.getData()).thenReturn(new JSONArray().put(data));

    emailService.sendOrderConfirmationEmail(user, order);

    assertEquals(200, mailjetResponse.getStatus());
    assertEquals(user.getEmail(), mailjetResponse.getData().getJSONObject(0).getString("Email"));
  }

  @Test
  public void testSendPasswordConfirmationEmail() throws JSONException, MailjetSocketTimeoutException,
    MailjetException {
    User user = UserFactory.getDefaultUser();
    JSONObject data = new JSONObject();
    when(mailjetResponse.getStatus()).thenReturn(200);
    data.put("Email", user.getEmail());
    when(mailjetResponse.getData()).thenReturn(new JSONArray().put(data));

    emailService.sendPasswordConfirmationEmail(user, UserConstants.PASSWORD);

    assertEquals(200, mailjetResponse.getStatus());
    assertEquals(user.getEmail(), mailjetResponse.getData().getJSONObject(0).getString("Email"));
  }

  @Test
  public void testSendRegistrationConfirmationEmail() throws JSONException, MailjetSocketTimeoutException, MailjetException {

    User user = UserFactory.getDefaultUser();

    JSONObject data = new JSONObject();
    when(mailjetResponse.getStatus()).thenReturn(200);
    data.put("Email", user.getEmail());
    when(mailjetResponse.getData()).thenReturn(new JSONArray().put(data));

    emailService.sendRegistrationConfirmationEmail(user);

    assertEquals(200, mailjetResponse.getStatus());
    assertEquals(user.getEmail(), mailjetResponse.getData().getJSONObject(0).getString("Email"));
  }
}




