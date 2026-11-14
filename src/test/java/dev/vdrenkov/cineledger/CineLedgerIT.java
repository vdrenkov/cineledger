package dev.vdrenkov.cineledger;

import dev.vdrenkov.cineledger.services.EmailService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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

    @MockitoBean
    private java.time.Clock clock;

    @BeforeEach
    void setClock() {
        org.mockito.Mockito.when(clock.instant()).thenAnswer(_ -> java.time.Instant.now());
    }

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

    @Test
    void passwordRecovery_validToken_changesPasswordOnceAndRevokesOldAuthentication() throws Exception {
        final String username = "reset" + UUID.randomUUID().toString().replace("-", "");
        final MvcResult created = mockMvc.perform(withCsrf(post("/users")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("username", username, "password", PASSWORD,
                "email", username + "@example.test", "firstName", "Test", "lastName", "Customer"))))
            .andExpect(status().isCreated())
            .andReturn();
        final String profile = created.getResponse().getHeader("Location");
        final Session original = login(username);
        mockMvc.perform(withCsrf(post("/password-reset-requests")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("username", username))))
            .andExpect(status().isAccepted());
        final var token = org.mockito.ArgumentCaptor.forClass(String.class);
        org.mockito.Mockito.verify(emailService).sendPasswordResetEmail(org.mockito.ArgumentMatchers.any(), token.capture());
        assertEquals(43, token.getValue().length());
        login(username);
        final String replacement = "ChangedPassword123";
        mockMvc.perform(withCsrf(post("/password-resets")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("token", token.getValue(), "password", replacement))))
            .andExpect(status().isNoContent());
        mockMvc.perform(authenticated(get(profile), original)).andExpect(status().isForbidden());
        mockMvc.perform(withCsrf(post("/login")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("username", username, "password", PASSWORD))))
            .andExpect(status().isUnauthorized());
        mockMvc.perform(withCsrf(post("/login")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("username", username, "password", replacement))))
            .andExpect(status().isOk());
        mockMvc.perform(withCsrf(post("/password-resets")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("token", token.getValue(), "password", replacement))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void passwordRecovery_unknownAccount_returnsSameAcceptedResponse() throws Exception {
        mockMvc.perform(withCsrf(post("/password-reset-requests")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("username", "unknown-customer"))))
            .andExpect(status().isAccepted());
        org.mockito.Mockito.verify(emailService, org.mockito.Mockito.never())
            .sendPasswordResetEmail(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void passwordRecovery_unknownToken_isRejected() throws Exception {
        mockMvc.perform(withCsrf(post("/password-resets")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("token", "a".repeat(43), "password", PASSWORD))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void passwordRecovery_expiredToken_preservesOriginalPassword() throws Exception {
        final RecoveryFixture fixture = recoveryFixture();
        org.mockito.Mockito.when(clock.instant()).thenReturn(java.time.Instant.now().plusSeconds(1801));

        mockMvc.perform(withCsrf(post("/password-resets")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("token", fixture.token(), "password", "ChangedPassword123"))))
            .andExpect(status().isBadRequest());
        login(fixture.username());
    }

    @Test
    void passwordRecovery_concurrentTokenUse_succeedsExactlyOnce() throws Exception {
        final RecoveryFixture fixture = recoveryFixture();
        final var start = new java.util.concurrent.CountDownLatch(1);
        try (final var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            final var attempts = java.util.stream.IntStream.range(0, 2).mapToObj(_ -> executor.submit(() -> {
                start.await();
                return mockMvc.perform(withCsrf(post("/password-resets")).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of("token", fixture.token(), "password", "ChangedPassword123"))))
                    .andReturn().getResponse().getStatus();
            })).toList();
            start.countDown();
            final var statuses = new java.util.ArrayList<Integer>();
            for (final var attempt : attempts) {
                statuses.add(attempt.get(20, java.util.concurrent.TimeUnit.SECONDS));
            }
            assertEquals(java.util.List.of(204, 400), statuses.stream().sorted().toList());
        }
    }

    private RecoveryFixture recoveryFixture() throws Exception {
        final String username = "recovery" + UUID.randomUUID().toString().replace("-", "");
        mockMvc.perform(withCsrf(post("/users")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("username", username, "password", PASSWORD,
                "email", username + "@example.test", "firstName", "Test", "lastName", "Customer"))))
            .andExpect(status().isCreated());
        mockMvc.perform(withCsrf(post("/password-reset-requests")).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("username", username))))
            .andExpect(status().isAccepted());
        final var token = org.mockito.ArgumentCaptor.forClass(String.class);
        org.mockito.Mockito.verify(emailService).sendPasswordResetEmail(org.mockito.ArgumentMatchers.any(), token.capture());
        return new RecoveryFixture(username, token.getValue());
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
    private record RecoveryFixture(String username, String token) {}
}
