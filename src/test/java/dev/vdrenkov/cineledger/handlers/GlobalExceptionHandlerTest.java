package dev.vdrenkov.cineledger.handlers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.vdrenkov.cineledger.controllers.CategoryController;
import dev.vdrenkov.cineledger.controllers.MovieController;
import dev.vdrenkov.cineledger.controllers.ProjectionController;
import dev.vdrenkov.cineledger.controllers.UserController;
import dev.vdrenkov.cineledger.exceptions.CategoryAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CategoryNotFoundException;
import dev.vdrenkov.cineledger.exceptions.CinemaAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CinemaNotFoundException;
import dev.vdrenkov.cineledger.exceptions.DateNotValidException;
import dev.vdrenkov.cineledger.exceptions.DiscountAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.DiscountNotFoundException;
import dev.vdrenkov.cineledger.exceptions.DiscountNotValidException;
import dev.vdrenkov.cineledger.exceptions.HallNotAvailableException;
import dev.vdrenkov.cineledger.exceptions.HallNotFoundException;
import dev.vdrenkov.cineledger.exceptions.ItemAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.ItemNotFoundException;
import dev.vdrenkov.cineledger.exceptions.MovieAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.MovieNotFoundException;
import dev.vdrenkov.cineledger.exceptions.NoAvailableItemsException;
import dev.vdrenkov.cineledger.exceptions.NoAvailableTicketsException;
import dev.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import dev.vdrenkov.cineledger.exceptions.NotLoggedInException;
import dev.vdrenkov.cineledger.exceptions.OrderNotFoundException;
import dev.vdrenkov.cineledger.exceptions.ProgramAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.ProgramNotFoundException;
import dev.vdrenkov.cineledger.exceptions.ProjectionNotFoundException;
import dev.vdrenkov.cineledger.exceptions.ReviewNotFoundException;
import dev.vdrenkov.cineledger.exceptions.RoleAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.RoleNotChosenException;
import dev.vdrenkov.cineledger.exceptions.RoleNotFoundException;
import dev.vdrenkov.cineledger.exceptions.TicketNotFoundException;
import dev.vdrenkov.cineledger.exceptions.UserEmailAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.UserNotFoundException;
import dev.vdrenkov.cineledger.exceptions.UsernameAlreadyExistsException;
import dev.vdrenkov.cineledger.models.requests.CategoryRequest;
import dev.vdrenkov.cineledger.services.CategoryService;
import dev.vdrenkov.cineledger.services.MovieService;
import dev.vdrenkov.cineledger.services.ProjectionService;
import dev.vdrenkov.cineledger.services.UserService;
import dev.vdrenkov.cineledger.testutils.constants.UserConstants;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import jakarta.validation.UnexpectedTypeException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import tools.jackson.databind.ObjectMapper;

import java.time.format.DateTimeParseException;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    private static final String URI = "/categories";
    private static final String ROOT_ERRORS = "$.Errors";

    private MockMvc mockMvc;

    @Mock private CategoryService categoryService;

    @InjectMocks private CategoryController categoryController;

    @BeforeEach
    void setup() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(categoryController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();
    }

    @Test
    void testHandleMethodArgumentNotValidException_onEndpointGetAllOrders_badRequest()
            throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(new CategoryRequest(null));

        mockMvc.perform(post(URI).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleInternalAuthenticationServiceException_onEndpointGetAllOrders_badRequest()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(InternalAuthenticationServiceException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleBadCredentialsException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(BadCredentialsException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isUnauthorized())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.AUTHENTICATION_FAILED_MESSAGE)));
    }

    @Test
    void testHandleNullPointerException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(NullPointerException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleUnexpectedTypeException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(UnexpectedTypeException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleHttpMessageNotReadableException_onEndpointGetAllOrders_badRequest()
            throws Exception {
        when(categoryService.getAllCategories()).thenThrow(HttpMessageNotReadableException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleHttpRequestMethodNotSupportedException_onEndpointGetAllOrders_badRequest()
            throws Exception {
        mockMvc.perform(patch(URI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleIllegalArgumentException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.INVALID_REQUEST_MESSAGE)));
    }

    @Test
    void testHandleMissingServletRequestParameterException_onEndpointGetAllOrders_badRequest()
            throws Exception {
        final MovieService movieService = Mockito.mock(MovieService.class);
        final MovieController movieController = new MovieController(movieService);

        MockMvc movieMockMvc =
                MockMvcBuilders.standaloneSetup(movieController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();

        movieMockMvc
                .perform(MockMvcRequestBuilders.get("/movies"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleMissingPathVariableException_onEndpointGetAllMoviesByCategoryId_badRequest()
            throws Exception {
        final MovieService movieService = Mockito.mock(MovieService.class);
        final MovieController movieController = new MovieController(movieService);

        MockMvc movieMockMvc =
                MockMvcBuilders.standaloneSetup(movieController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();

        movieMockMvc
                .perform(MockMvcRequestBuilders.get("/categories/ /movies"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleUnsatisfiedServletRequestParameterException_onEndpointGetAllOrders_badRequest()
            throws Exception {
        final UserService userService = Mockito.mock(UserService.class);
        final UserController userController = new UserController(userService);

        MockMvc movieMockMvc =
                MockMvcBuilders.standaloneSetup(userController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();

        movieMockMvc
                .perform(
                        MockMvcRequestBuilders.get("/users")
                                .queryParam("joinDate", String.valueOf(UserConstants.JOIN_DATE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleDateTimeParseException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(DateTimeParseException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException_onEndpointGetAllOrders_badRequest()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(MethodArgumentTypeMismatchException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleDataIntegrityViolationException_onEndpointGetAllOrders_badRequest()
            throws Exception {
        when(categoryService.getAllCategories()).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleIllegalArgumentException_onEndpointGetAllCategories_badRequest()
            throws Exception {
        when(categoryService.getAllCategories()).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void
            testHandleMissingServletRequestParameterException_onEndpointGetMoviesByReleaseDate_badRequest()
                    throws Exception {
        final ProjectionService projectionService = Mockito.mock(ProjectionService.class);
        final ProjectionController projectionController =
                new ProjectionController(projectionService);

        MockMvc projectionMockMvc =
                MockMvcBuilders.standaloneSetup(projectionController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();

        projectionMockMvc
                .perform(
                        get("/projections")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("isBefore", String.valueOf(true)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    void testHandleCategoryAlreadyExistsException_onEndPointGetAllCategories_conflict()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new CategoryAlreadyExistsException(
                                ExceptionMessages.CATEGORY_ALREADY_EXISTS_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.CATEGORY_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    void testHandleCinemaAlreadyExistsException_onEndPointGetAllCategories_conflict()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new CinemaAlreadyExistsException(
                                ExceptionMessages.CINEMA_ALREADY_EXISTS_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.CINEMA_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    void testHandleProgramAlreadyExistsException_onEndPointGetAllCategories_conflict()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new ProgramAlreadyExistsException(
                                ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    void testHandleRoleAlreadyExistsException_onEndPointGetAllCategories_conflict()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new RoleAlreadyExistsException(
                                ExceptionMessages.ROLE_ALREADY_EXISTS_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.ROLE_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    void testHandleUserEmailAlreadyExistsException_onEndPointGetAllCategories_conflict()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new UserEmailAlreadyExistsException(
                                ExceptionMessages.USER_EMAIL_ALREADY_EXISTS_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.USER_EMAIL_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    void testHandleUsernameAlreadyExistsException_onEndPointGetAllCategories_conflict()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new UsernameAlreadyExistsException(
                                ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    void testHandleProjectionNotFoundException_onEndPointGetAllCategories_notFound()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new ProjectionNotFoundException(
                                ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleCategoryNotFoundException_onEndpointGetAllCategories_notFound()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new CategoryNotFoundException(
                                ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleCinemaNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new CinemaNotFoundException(ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleProgramNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new ProgramNotFoundException(ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleUserNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.USER_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleDateNotValidException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.DATE_NOT_VALID_MESSAGE)));
    }

    @Test
    void testHandleHallNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new HallNotFoundException(ExceptionMessages.HALL_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.HALL_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleItemNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new ItemNotFoundException(ExceptionMessages.ITEM_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.ITEM_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleExistingItemException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new ItemAlreadyExistsException(
                                ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    void testHandleExistingMovieException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new MovieAlreadyExistsException(
                                ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    void testHandleOrderNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.ORDER_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleRoleNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.ROLE_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleRoleNotChosenException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new RoleNotChosenException(ExceptionMessages.ROLE_NOT_CHOSEN_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.ROLE_NOT_CHOSEN_MESSAGE)));
    }

    @Test
    void testHandleMovieNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleDiscountNotFoundException_onEndpointGetAllCategories_notFound()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new DiscountNotFoundException(
                                ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandeDiscountNotValidException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new DiscountNotValidException(
                                ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE)));
    }

    @Test
    void testHandleReviewNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new ReviewNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHandleTicketNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new TicketNotFoundException(ExceptionMessages.TICKET_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isNotFound())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.TICKET_NOT_FOUND_MESSAGE)));
    }

    @Test
    void testHallNotAvailableException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new HallNotAvailableException(
                                ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION));

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION)));
    }

    @Test
    void testNoAvailableTicketsException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new NoAvailableTicketsException(
                                ExceptionMessages.NO_AVAILABLE_TICKETS_EXCEPTION));

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.NO_AVAILABLE_TICKETS_EXCEPTION)));
    }

    @Test
    void testNoAvailableItemsException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new NoAvailableItemsException(
                                ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION));

        mockMvc.perform(get(URI))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION)));
    }

    @Test
    void testHandleNotAuthorizedException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isForbidden())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.NOT_AUTHORIZED_MESSAGE)));
    }

    @Test
    void testHandleNotLoggedInException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(new NotLoggedInException(ExceptionMessages.NOT_LOGGED_IN_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isUnauthorized())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.NOT_LOGGED_IN_MESSAGE)));
    }

    @Test
    void testHandleDiscountAlreadyExistsException_onEndpointGetAllCategories_conflict()
            throws Exception {
        when(categoryService.getAllCategories())
                .thenThrow(
                        new DiscountAlreadyExistsException(
                                ExceptionMessages.DISCOUNT_ALREADY_EXISTS_MESSAGE));

        mockMvc.perform(get(URI))
                .andExpect(status().isConflict())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(
                                        ExceptionMessages.DISCOUNT_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    void testHandleException_onEndpointGetAllCategories_internalServerError() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(IllegalStateException.class);

        mockMvc.perform(get(URI))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                ROOT_ERRORS,
                                Matchers.containsInAnyOrder(ExceptionMessages.GLOBAL_EXCEPTION)));
    }
}
