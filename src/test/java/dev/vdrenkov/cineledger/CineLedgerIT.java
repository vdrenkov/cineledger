package dev.vdrenkov.cineledger;

import dev.vdrenkov.cineledger.services.EmailService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "jwt.secret=integration-tests-use-a-dedicated-signing-secret-of-at-least-sixty-four-bytes",
    "jwt.cookie.secure=false",
    "spring.datasource.password=managed-by-testcontainers",
    "app.bootstrap.admin.username=integration_admin",
    "app.bootstrap.admin.password=IntegrationPassword123",
    "app.bootstrap.admin.email=admin@example.test"
})
@AutoConfigureMockMvc
@Testcontainers
class CineLedgerIT {
    private static final String PASSWORD = "IntegrationPassword123";

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer(DockerImageName.parse(
        "cgr.dev/chainguard/postgres@sha256:69ae93d5150b50f20b5fa5d13914e33f04aa54a30edecf936788af663ee4f867")
        .asCompatibleSubstituteFor("postgres"))
        .withCommand("-c", "fsync=off")
        .waitingFor(Wait.forListeningPort());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmailService emailService;

    @Test
    void catalogue_multiDigitNestedId_remainsPublic() throws Exception {
        mockMvc.perform(get("/cinemas/123/halls")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void tickets_customerWithMultiDigitProjectionId_cannotCreateStaffTickets() throws Exception {
        mockMvc.perform(withCsrf(post("/projections/123/tickets"))
            .contentType(MediaType.APPLICATION_JSON).content("{\"projectionId\":123}"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void users_customerSearchingOtherAccounts_isForbidden() throws Exception {
        mockMvc.perform(get("/users").param("username", "integration_admin"))
            .andExpect(status().isForbidden());
    }

    @Test
    void csrf_tokenCookie_isHttpOnly() throws Exception {
        mockMvc.perform(get("/csrf")).andExpect(cookie().httpOnly("XSRF-TOKEN", true));
    }

    @Test
    void users_publicRegistration_createsOnlyCustomerRole() throws Exception {
        final String username = "customer" + UUID.randomUUID().toString().replace("-", "");
        final MvcResult result = mockMvc.perform(withCsrf(post("/users"))
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(Map.of(
                "username", username, "password", PASSWORD, "email", username + "@example.test",
                "firstName", "Test", "lastName", "Customer"))))
            .andExpect(status().isCreated())
            .andReturn();
        assertTrue(result.getResponse().getHeader("Location").startsWith("/users/"));
        final Session customer = login(username);
        final MvcResult profile = mockMvc.perform(authenticated(get(result.getResponse().getHeader("Location")), customer))
            .andExpect(status().isOk())
            .andReturn();
        assertEquals("USER", objectMapper.readTree(profile.getResponse().getContentAsString())
            .path("roles").get(0).path("name").asString());
    }

    @Test
    void users_publicRegistrationWithAdminRole_isForbidden() throws Exception {
        mockMvc.perform(withCsrf(post("/users")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("username", "attacker", "password", PASSWORD,
                "email", "attacker@example.test", "firstName", "Test", "lastName", "Customer",
                "roleNames", java.util.List.of("ADMIN")))))
            .andExpect(status().isForbidden());
    }

    private Session login(final String username) throws Exception {
        final MvcResult csrfResult = mockMvc.perform(get("/csrf")).andReturn();
        final String csrfToken = objectMapper.readTree(csrfResult.getResponse().getContentAsString()).path("token").asString();
        final Cookie csrfCookie = csrfResult.getResponse().getCookie("XSRF-TOKEN");
        final MvcResult response = mockMvc.perform(post("/login").cookie(csrfCookie)
            .header("X-XSRF-TOKEN", csrfToken).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("username", username, "password", PASSWORD))))
            .andExpect(status().isOk())
            .andReturn();
        return new Session(response.getResponse().getCookie("CINELEDGER_AUTH"), csrfCookie, csrfToken);
    }

    private MockHttpServletRequestBuilder withCsrf(final MockHttpServletRequestBuilder request) throws Exception {
        final MvcResult response = mockMvc.perform(get("/csrf")).andReturn();
        return request.cookie(response.getResponse().getCookie("XSRF-TOKEN"))
            .header("X-XSRF-TOKEN", objectMapper.readTree(response.getResponse().getContentAsString()).path("token").asString());
    }

    private MockHttpServletRequestBuilder authenticated(final MockHttpServletRequestBuilder request, final Session session) {
        return request.cookie(session.authentication(), session.csrfCookie()).header("X-XSRF-TOKEN", session.csrfToken());
    }

    private record Session(Cookie authentication, Cookie csrfCookie, String csrfToken) {}
}
