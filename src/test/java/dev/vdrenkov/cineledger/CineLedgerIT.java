package dev.vdrenkov.cineledger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.vdrenkov.cineledger.services.EmailService;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
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
    private static final java.util.concurrent.atomic.AtomicInteger DISCOUNT_CODE =
            new java.util.concurrent.atomic.AtomicInteger(1000);
    private static final String PASSWORD = "IntegrationPassword123";

    @Container @ServiceConnection
    private static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer(
                            DockerImageName.parse(
                                            "cgr.dev/chainguard/postgres@sha256:69ae93d5150b50f20b5fa5d13914e33f04aa54a30edecf936788af663ee4f867")
                                    .asCompatibleSubstituteFor("postgres"))
                    .withCommand("-c", "fsync=off")
                    .waitingFor(Wait.forListeningPort());

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private EmailService emailService;

    @MockitoBean private java.time.Clock clock;

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
        mockMvc.perform(
                        withCsrf(post("/projections/123/tickets"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"projectionId\":123}"))
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
        final MvcResult result =
                mockMvc.perform(
                                withCsrf(post("/users"))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                objectMapper.writeValueAsString(
                                                        Map.of(
                                                                "username",
                                                                username,
                                                                "password",
                                                                PASSWORD,
                                                                "email",
                                                                username + "@example.test",
                                                                "firstName",
                                                                "Test",
                                                                "lastName",
                                                                "Customer"))))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getHeader("Location").startsWith("/users/"));
        final Session customer = login(username);
        final MvcResult profile =
                mockMvc.perform(
                                authenticated(
                                        get(result.getResponse().getHeader("Location")), customer))
                        .andExpect(status().isOk())
                        .andReturn();
        assertEquals(
                "USER",
                objectMapper
                        .readTree(profile.getResponse().getContentAsString())
                        .path("roles")
                        .get(0)
                        .path("name")
                        .asString());
    }

    @Test
    void users_publicRegistrationWithAdminRole_isForbidden() throws Exception {
        mockMvc.perform(
                        withCsrf(post("/users"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "username",
                                                        "attacker",
                                                        "password",
                                                        PASSWORD,
                                                        "email",
                                                        "attacker@example.test",
                                                        "firstName",
                                                        "Test",
                                                        "lastName",
                                                        "Customer",
                                                        "roleNames",
                                                        java.util.List.of("ADMIN")))))
                .andExpect(status().isForbidden());
    }

    @Test
    void passwordRecovery_validToken_changesPasswordOnceAndRevokesOldAuthentication()
            throws Exception {
        final String username = "reset" + UUID.randomUUID().toString().replace("-", "");
        final MvcResult created =
                mockMvc.perform(
                                withCsrf(post("/users"))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                objectMapper.writeValueAsString(
                                                        Map.of(
                                                                "username",
                                                                username,
                                                                "password",
                                                                PASSWORD,
                                                                "email",
                                                                username + "@example.test",
                                                                "firstName",
                                                                "Test",
                                                                "lastName",
                                                                "Customer"))))
                        .andExpect(status().isCreated())
                        .andReturn();
        final String profile = created.getResponse().getHeader("Location");
        final Session original = login(username);
        mockMvc.perform(
                        withCsrf(post("/password-reset-requests"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of("username", username))))
                .andExpect(status().isAccepted());
        final var token = org.mockito.ArgumentCaptor.forClass(String.class);
        org.mockito.Mockito.verify(emailService)
                .sendPasswordResetEmail(org.mockito.ArgumentMatchers.any(), token.capture());
        assertEquals(43, token.getValue().length());
        login(username);
        final String replacement = "ChangedPassword123";
        mockMvc.perform(
                        withCsrf(post("/password-resets"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "token",
                                                        token.getValue(),
                                                        "password",
                                                        replacement))))
                .andExpect(status().isNoContent());
        mockMvc.perform(authenticated(get(profile), original)).andExpect(status().isForbidden());
        mockMvc.perform(
                        withCsrf(post("/login"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "username",
                                                        username,
                                                        "password",
                                                        PASSWORD))))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(
                        withCsrf(post("/login"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "username",
                                                        username,
                                                        "password",
                                                        replacement))))
                .andExpect(status().isOk());
        mockMvc.perform(
                        withCsrf(post("/password-resets"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "token",
                                                        token.getValue(),
                                                        "password",
                                                        replacement))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void passwordRecovery_unknownAccount_returnsSameAcceptedResponse() throws Exception {
        mockMvc.perform(
                        withCsrf(post("/password-reset-requests"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of("username", "unknown-customer"))))
                .andExpect(status().isAccepted());
        org.mockito.Mockito.verify(emailService, org.mockito.Mockito.never())
                .sendPasswordResetEmail(
                        org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void passwordRecovery_unknownToken_isRejected() throws Exception {
        mockMvc.perform(
                        withCsrf(post("/password-resets"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "token",
                                                        "a".repeat(43),
                                                        "password",
                                                        PASSWORD))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void passwordRecovery_expiredToken_preservesOriginalPassword() throws Exception {
        final RecoveryFixture fixture = recoveryFixture();
        org.mockito.Mockito.when(clock.instant())
                .thenReturn(java.time.Instant.now().plusSeconds(1801));

        mockMvc.perform(
                        withCsrf(post("/password-resets"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "token",
                                                        fixture.token(),
                                                        "password",
                                                        "ChangedPassword123"))))
                .andExpect(status().isBadRequest());
        login(fixture.username());
    }

    @Test
    void passwordRecovery_concurrentTokenUse_succeedsExactlyOnce() throws Exception {
        final RecoveryFixture fixture = recoveryFixture();
        final var start = new java.util.concurrent.CountDownLatch(1);
        try (final var executor =
                java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            final var attempts =
                    java.util.stream.IntStream.range(0, 2)
                            .mapToObj(_ ->
                                    executor.submit(() -> {
                                        start.await();
                                        return mockMvc.perform(
                                                        withCsrf(post("/password-resets"))
                                                                .contentType(
                                                                        MediaType.APPLICATION_JSON)
                                                                .content(
                                                                        objectMapper
                                                                                .writeValueAsString(
                                                                                        Map.of(
                                                                                                "token",
                                                                                                fixture
                                                                                                        .token(),
                                                                                                "password",
                                                                                                "ChangedPassword123"))))
                                                .andReturn()
                                                .getResponse()
                                                .getStatus();
                                    }))
                            .toList();
            start.countDown();
            final var statuses = new java.util.ArrayList<Integer>();
            for (final var attempt : attempts) {
                statuses.add(attempt.get(20, java.util.concurrent.TimeUnit.SECONDS));
            }
            assertEquals(java.util.List.of(204, 400), statuses.stream().sorted().toList());
        }
    }

    @Test
    void reservations_missingDiscount_createsOrder() throws Exception {
        final Session admin = login("integration_admin");
        final Fixture fixture = fixture(admin, 1, 1);
        mockMvc.perform(
                        authenticated(post("/users/" + fixture.customerId() + "/orders"), admin)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                java.util.List.of(
                                                        Map.of(
                                                                "projectionId",
                                                                fixture.projectionId())))))
                .andExpect(status().isCreated());
    }

    @Test
    void reservations_lastSeat_concurrentRequestsSucceedOnce() throws Exception {
        final Session admin = login("integration_admin");
        final Fixture fixture = fixture(admin, 1, 1);
        final var start = new java.util.concurrent.CountDownLatch(1);
        try (final var executor =
                java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            final var attempts =
                    java.util.stream.IntStream.range(0, 6)
                            .mapToObj(_ ->
                                    executor.submit(() -> {
                                        start.await();
                                        return mockMvc.perform(
                                                        authenticated(
                                                                        post(
                                                                                "/users/"
                                                                                        + fixture
                                                                                                .customerId()
                                                                                        + "/orders"),
                                                                        admin)
                                                                .param(
                                                                        "discountCode",
                                                                        fixture.discountCode())
                                                                .contentType(
                                                                        MediaType.APPLICATION_JSON)
                                                                .content(
                                                                        objectMapper
                                                                                .writeValueAsString(
                                                                                        java.util
                                                                                                .List
                                                                                                .of(
                                                                                                        Map
                                                                                                                .of(
                                                                                                                        "projectionId",
                                                                                                                        fixture
                                                                                                                                .projectionId())))))
                                                .andReturn()
                                                .getResponse()
                                                .getStatus();
                                    }))
                            .toList();
            start.countDown();
            final var statuses = new java.util.ArrayList<Integer>();
            for (final var attempt : attempts)
                statuses.add(attempt.get(30, java.util.concurrent.TimeUnit.SECONDS));
            assertEquals(1, statuses.stream().filter(status -> status == 201).count());
            assertTrue(statuses.stream().allMatch(status -> status == 201 || status == 400));
        }
    }

    @Test
    void cancelOrder_existingPurchase_releasesStockAndSeat() throws Exception {
        final Session admin = login("integration_admin");
        final Fixture fixture = fixture(admin, 1, 1);
        final int ticketId =
                create(admin, "/tickets", Map.of("projectionId", fixture.projectionId()));
        final int orderId =
                create(
                        admin,
                        "/orders",
                        Map.of(
                                "userId",
                                fixture.customerId(),
                                "ticketsIds",
                                java.util.List.of(ticketId),
                                "itemsIds",
                                java.util.List.of(fixture.itemId())));
        mockMvc.perform(
                        authenticated(
                                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                        .delete("/orders/" + orderId),
                                admin))
                .andExpect(status().isNoContent());
        mockMvc.perform(authenticated(get("/items").param("itemName", fixture.itemName()), admin))
                .andExpect(status().isOk())
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath(
                                        "$.quantity")
                                .value(1));
        create(admin, "/tickets", Map.of("projectionId", fixture.projectionId()));
    }

    @Test
    void order_unavailableSecondItem_rollsBackStock() throws Exception {
        final Session admin = login("integration_admin");
        final Fixture fixture = fixture(admin, 2, 1);
        final int ticketId =
                create(admin, "/tickets", Map.of("projectionId", fixture.projectionId()));
        mockMvc.perform(
                        authenticated(post("/orders"), admin)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "userId",
                                                        fixture.customerId(),
                                                        "ticketsIds",
                                                        java.util.List.of(ticketId),
                                                        "itemsIds",
                                                        java.util.List.of(
                                                                fixture.itemId(),
                                                                fixture.itemId())))))
                .andExpect(status().isBadRequest());
        mockMvc.perform(authenticated(get("/items").param("itemName", fixture.itemName()), admin))
                .andExpect(status().isOk())
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath(
                                        "$.quantity")
                                .value(1));
    }

    @Test
    void projection_updatingItsOwnSchedule_doesNotConflictWithItself() throws Exception {
        final Session admin = login("integration_admin");
        final Fixture fixture = fixture(admin, 2, 1);
        mockMvc.perform(
                        authenticated(
                                        org.springframework.test.web.servlet.request
                                                .MockMvcRequestBuilders.put(
                                                "/projections/" + fixture.projectionId()),
                                        admin)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                projection(fixture, "18:00:00"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void projection_backToBackFilms_doNotOverlap() throws Exception {
        final Session admin = login("integration_admin");
        final Fixture fixture = fixture(admin, 2, 1);
        create(admin, "/projections", projection(fixture, "20:00:00"));
    }

    @Test
    void order_duplicateTicketIds_isRejectedBeforePersistence() throws Exception {
        final Session admin = login("integration_admin");
        final Fixture fixture = fixture(admin, 2, 1);
        final int ticketId =
                create(admin, "/tickets", Map.of("projectionId", fixture.projectionId()));
        mockMvc.perform(
                        authenticated(post("/orders"), admin)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "userId",
                                                        fixture.customerId(),
                                                        "ticketsIds",
                                                        java.util.List.of(ticketId, ticketId)))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void orders_lastItem_concurrentPurchasesSucceedOnce() throws Exception {
        final Session admin = login("integration_admin");
        final Fixture fixture = fixture(admin, 6, 1);
        final var tickets = new java.util.ArrayList<Integer>();
        for (int index = 0; index < 6; index++) {
            tickets.add(create(admin, "/tickets", Map.of("projectionId", fixture.projectionId())));
        }
        final var start = new java.util.concurrent.CountDownLatch(1);
        try (final var executor =
                java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            final var attempts =
                    tickets.stream()
                            .map(ticketId ->
                                    executor.submit(() -> {
                                        start.await();
                                        return mockMvc.perform(
                                                        authenticated(post("/orders"), admin)
                                                                .contentType(
                                                                        MediaType.APPLICATION_JSON)
                                                                .content(
                                                                        objectMapper
                                                                                .writeValueAsString(
                                                                                        Map.of(
                                                                                                "userId",
                                                                                                fixture
                                                                                                        .customerId(),
                                                                                                "ticketsIds",
                                                                                                java
                                                                                                        .util
                                                                                                        .List
                                                                                                        .of(
                                                                                                                ticketId),
                                                                                                "itemsIds",
                                                                                                java
                                                                                                        .util
                                                                                                        .List
                                                                                                        .of(
                                                                                                                fixture
                                                                                                                        .itemId())))))
                                                .andReturn()
                                                .getResponse()
                                                .getStatus();
                                    }))
                            .toList();
            start.countDown();
            final var statuses = new java.util.ArrayList<Integer>();
            for (final var attempt : attempts)
                statuses.add(attempt.get(30, java.util.concurrent.TimeUnit.SECONDS));
            assertEquals(1, statuses.stream().filter(status -> status == 201).count());
            assertTrue(statuses.stream().allMatch(status -> status == 201 || status == 400));
        }
    }

    @Test
    void projections_overlapAcrossProgramDates_isRejected() throws Exception {
        final Session admin = login("integration_admin");
        final Fixture fixture = fixture(admin, 1, 1);
        create(admin, "/projections", projection(fixture, "23:00:00"));
        final int nextProgram =
                create(
                        admin,
                        "/programs",
                        Map.of(
                                "programDate",
                                java.time.LocalDate.now().plusDays(3).toString(),
                                "cinemaId",
                                fixture.cinemaId()));
        mockMvc.perform(
                        authenticated(post("/projections"), admin)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "price",
                                                        10.25,
                                                        "hallId",
                                                        fixture.hallId(),
                                                        "programId",
                                                        nextProgram,
                                                        "movieId",
                                                        fixture.movieId(),
                                                        "startTime",
                                                        "00:30:00"))))
                .andExpect(status().isBadRequest());
    }

    private Fixture fixture(final Session admin, final int capacity, final int quantity)
            throws Exception {
        final String suffix = UUID.randomUUID().toString().replace("-", "");
        final int categoryId = create(admin, "/categories", Map.of("name", "Category" + suffix));
        final int cinemaId =
                create(admin, "/cinemas", Map.of("address", "Address " + suffix, "city", "Sofia"));
        final int hallId = create(admin, "/halls", Map.of("capacity", 21, "cinemaId", cinemaId));
        final int movieId =
                create(
                        admin,
                        "/movies",
                        Map.of(
                                "title",
                                "Movie" + suffix,
                                "description",
                                "Test movie",
                                "releaseDate",
                                java.time.LocalDate.now().plusDays(1).toString(),
                                "runtime",
                                "PT2H",
                                "categoryId",
                                categoryId));
        final int programId =
                create(
                        admin,
                        "/programs",
                        Map.of(
                                "programDate",
                                java.time.LocalDate.now().plusDays(2).toString(),
                                "cinemaId",
                                cinemaId));
        final int projectionId =
                create(
                        admin,
                        "/projections",
                        Map.of(
                                "price",
                                10.25,
                                "hallId",
                                hallId,
                                "programId",
                                programId,
                                "movieId",
                                movieId,
                                "startTime",
                                "18:00:00"));
        for (int booked = capacity; booked < 21; booked++) {
            create(admin, "/tickets", Map.of("projectionId", projectionId));
        }
        final String itemName = "Popcorn" + suffix;
        final int itemId =
                create(
                        admin,
                        "/items",
                        Map.of("name", itemName, "price", 2.75, "quantity", quantity));
        final int customerId =
                create(
                        admin,
                        "/users",
                        Map.of(
                                "username",
                                "user" + suffix,
                                "password",
                                PASSWORD,
                                "email",
                                suffix + "@example.test",
                                "firstName",
                                "Test",
                                "lastName",
                                "Customer"));
        final String discountCode = String.valueOf(DISCOUNT_CODE.incrementAndGet());
        create(
                admin,
                "/discounts",
                Map.of("type", "Discount" + suffix, "code", discountCode, "percentage", 10));
        return new Fixture(
                cinemaId,
                hallId,
                movieId,
                programId,
                projectionId,
                itemId,
                itemName,
                customerId,
                discountCode);
    }

    private Map<String, Object> projection(final Fixture fixture, final String startTime) {
        return Map.of(
                "price",
                10.25,
                "hallId",
                fixture.hallId(),
                "programId",
                fixture.programId(),
                "movieId",
                fixture.movieId(),
                "startTime",
                startTime);
    }

    private int create(final Session session, final String path, final Object body)
            throws Exception {
        final var response =
                mockMvc.perform(
                                authenticated(post(path), session)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(body)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse();
        final String location = response.getHeader("Location");
        return Integer.parseInt(location.substring(location.lastIndexOf('/') + 1));
    }

    private RecoveryFixture recoveryFixture() throws Exception {
        final String username = "recovery" + UUID.randomUUID().toString().replace("-", "");
        mockMvc.perform(
                        withCsrf(post("/users"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of(
                                                        "username",
                                                        username,
                                                        "password",
                                                        PASSWORD,
                                                        "email",
                                                        username + "@example.test",
                                                        "firstName",
                                                        "Test",
                                                        "lastName",
                                                        "Customer"))))
                .andExpect(status().isCreated());
        mockMvc.perform(
                        withCsrf(post("/password-reset-requests"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                Map.of("username", username))))
                .andExpect(status().isAccepted());
        final var token = org.mockito.ArgumentCaptor.forClass(String.class);
        org.mockito.Mockito.verify(emailService)
                .sendPasswordResetEmail(org.mockito.ArgumentMatchers.any(), token.capture());
        return new RecoveryFixture(username, token.getValue());
    }

    private Session login(final String username) throws Exception {
        final MvcResult csrfResult = mockMvc.perform(get("/csrf")).andReturn();
        final String csrfToken =
                objectMapper
                        .readTree(csrfResult.getResponse().getContentAsString())
                        .path("token")
                        .asString();
        final Cookie csrfCookie = csrfResult.getResponse().getCookie("XSRF-TOKEN");
        final MvcResult response =
                mockMvc.perform(
                                post("/login")
                                        .cookie(csrfCookie)
                                        .header("X-XSRF-TOKEN", csrfToken)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                objectMapper.writeValueAsString(
                                                        Map.of(
                                                                "username",
                                                                username,
                                                                "password",
                                                                PASSWORD))))
                        .andExpect(status().isOk())
                        .andReturn();
        return new Session(
                response.getResponse().getCookie("CINELEDGER_AUTH"), csrfCookie, csrfToken);
    }

    private MockHttpServletRequestBuilder withCsrf(final MockHttpServletRequestBuilder request)
            throws Exception {
        final MvcResult response = mockMvc.perform(get("/csrf")).andReturn();
        return request.cookie(response.getResponse().getCookie("XSRF-TOKEN"))
                .header(
                        "X-XSRF-TOKEN",
                        objectMapper
                                .readTree(response.getResponse().getContentAsString())
                                .path("token")
                                .asString());
    }

    private MockHttpServletRequestBuilder authenticated(
            final MockHttpServletRequestBuilder request, final Session session) {
        return request.cookie(session.authentication(), session.csrfCookie())
                .header("X-XSRF-TOKEN", session.csrfToken());
    }

    private record Session(Cookie authentication, Cookie csrfCookie, String csrfToken) {}

    private record Fixture(
            int cinemaId,
            int hallId,
            int movieId,
            int programId,
            int projectionId,
            int itemId,
            String itemName,
            int customerId,
            String discountCode) {}

    private record RecoveryFixture(String username, String token) {}
}
