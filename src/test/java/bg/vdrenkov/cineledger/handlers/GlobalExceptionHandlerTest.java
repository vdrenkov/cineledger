package bg.vdrenkov.cineledger.handlers;

import bg.vdrenkov.cineledger.controllers.CategoryController;
import bg.vdrenkov.cineledger.controllers.MovieController;
import bg.vdrenkov.cineledger.controllers.ProjectionController;
import bg.vdrenkov.cineledger.controllers.UserController;
import bg.vdrenkov.cineledger.exceptions.CategoryAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.CategoryNotFoundException;
import bg.vdrenkov.cineledger.exceptions.CinemaAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.CinemaNotFoundException;
import bg.vdrenkov.cineledger.exceptions.DateNotValidException;
import bg.vdrenkov.cineledger.exceptions.DiscountAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.DiscountNotFoundException;
import bg.vdrenkov.cineledger.exceptions.DiscountNotValidException;
import bg.vdrenkov.cineledger.exceptions.HallNotAvailableException;
import bg.vdrenkov.cineledger.exceptions.HallNotFoundException;
import bg.vdrenkov.cineledger.exceptions.ItemAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.ItemNotFoundException;
import bg.vdrenkov.cineledger.exceptions.MovieAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.MovieNotFoundException;
import bg.vdrenkov.cineledger.exceptions.NoAvailableItemsException;
import bg.vdrenkov.cineledger.exceptions.NoAvailableTicketsException;
import bg.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import bg.vdrenkov.cineledger.exceptions.NotLoggedInException;
import bg.vdrenkov.cineledger.exceptions.OrderNotFoundException;
import bg.vdrenkov.cineledger.exceptions.ProgramAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.ProgramNotFoundException;
import bg.vdrenkov.cineledger.exceptions.ProjectionNotFoundException;
import bg.vdrenkov.cineledger.exceptions.ReviewNotFoundException;
import bg.vdrenkov.cineledger.exceptions.RoleAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.RoleNotChosenException;
import bg.vdrenkov.cineledger.exceptions.RoleNotFoundException;
import bg.vdrenkov.cineledger.exceptions.TicketNotFoundException;
import bg.vdrenkov.cineledger.exceptions.UserEmailAlreadyExistsException;
import bg.vdrenkov.cineledger.exceptions.UserNotFoundException;
import bg.vdrenkov.cineledger.exceptions.UsernameAlreadyExistsException;
import bg.vdrenkov.cineledger.models.requests.CategoryRequest;
import bg.vdrenkov.cineledger.services.CategoryService;
import bg.vdrenkov.cineledger.services.MovieService;
import bg.vdrenkov.cineledger.services.ProjectionService;
import bg.vdrenkov.cineledger.services.UserService;
import bg.vdrenkov.cineledger.testUtils.constants.UserConstants;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private final String URI = "/categories";

    private final String ROOT_ERRORS = "$.Errors";

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(categoryController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    public void testHandleMethodArgumentNotValidException_onEndpointGetAllOrders_badRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(new CategoryRequest());

        mockMvc
            .perform(post(URI).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleInternalAuthenticationServiceException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(InternalAuthenticationServiceException.class);

        mockMvc.perform(get(URI)).andExpect(status().isBadRequest()).andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleBadCredentialsException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(BadCredentialsException.class);

        mockMvc
            .perform(get(URI))
            .andExpect(status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.AUTHENTICATION_FAILED_MESSAGE)));
    }

    @Test
    public void testHandleNullPointerException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(NullPointerException.class);

        mockMvc.perform(get(URI)).andExpect(status().isInternalServerError()).andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleUnexpectedTypeException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(UnexpectedTypeException.class);

        mockMvc.perform(get(URI)).andExpect(status().isBadRequest()).andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleHttpMessageNotReadableException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(HttpMessageNotReadableException.class);

        mockMvc.perform(get(URI)).andExpect(status().isBadRequest()).andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleHttpRequestMethodNotSupportedException_onEndpointGetAllOrders_badRequest() throws Exception {
        mockMvc.perform(patch(URI)).andExpect(status().isMethodNotAllowed()).andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleIllegalArgumentException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(IllegalArgumentException.class);

        mockMvc
            .perform(get(URI))
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.INVALID_REQUEST_MESSAGE)));
    }

    @Test
    public void testHandleMissingServletRequestParameterException_onEndpointGetAllOrders_badRequest() throws Exception {
        MovieService movieService = Mockito.mock(MovieService.class);
        MovieController movieController = new MovieController(movieService);

        MockMvc movieMockMvc = MockMvcBuilders
            .standaloneSetup(movieController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

        movieMockMvc.perform(MockMvcRequestBuilders.get("/movies")).andExpect(status().isBadRequest());
    }

    @Test
    public void testHandleMissingPathVariableException_onEndpointGetAllMoviesByCategoryId_badRequest()
        throws Exception {
        MovieService movieService = Mockito.mock(MovieService.class);
        MovieController movieController = new MovieController(movieService);

        MockMvc movieMockMvc = MockMvcBuilders
            .standaloneSetup(movieController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

        movieMockMvc.perform(MockMvcRequestBuilders.get("/categories/ /movies")).andExpect(status().isBadRequest());
    }

    @Test
    public void testHandleUnsatisfiedServletRequestParameterException_onEndpointGetAllOrders_badRequest()
        throws Exception {
        UserService userService = Mockito.mock(UserService.class);
        UserController userController = new UserController(userService);

        MockMvc movieMockMvc = MockMvcBuilders
            .standaloneSetup(userController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

        movieMockMvc
            .perform(
                MockMvcRequestBuilders.get("/users").queryParam("joinDate", String.valueOf(UserConstants.JOIN_DATE)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleDateTimeParseException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(DateTimeParseException.class);

        mockMvc.perform(get(URI)).andExpect(status().isBadRequest()).andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleMethodArgumentTypeMismatchException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(MethodArgumentTypeMismatchException.class);

        mockMvc.perform(get(URI)).andExpect(status().isBadRequest()).andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleDataIntegrityViolationException_onEndpointGetAllOrders_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(get(URI)).andExpect(status().isConflict()).andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleIllegalArgumentException_onEndpointGetAllCategories_badRequest() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get(URI)).andExpect(status().isBadRequest()).andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleMissingServletRequestParameterException_onEndpointGetMoviesByReleaseDate_badRequest()
        throws Exception {
        ProjectionService projectionService = Mockito.mock(ProjectionService.class);
        ProjectionController projectionController = new ProjectionController(projectionService);

        MockMvc projectionMockMvc = MockMvcBuilders
            .standaloneSetup(projectionController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

        projectionMockMvc
            .perform(get("/projections").accept(MediaType.APPLICATION_JSON).param("isBefore", String.valueOf(true)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(ROOT_ERRORS).exists());
    }

    @Test
    public void testHandleCategoryAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new CategoryAlreadyExistsException(ExceptionMessages.CATEGORY_ALREADY_EXISTS_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.CATEGORY_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    public void testHandleCinemaAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new CinemaAlreadyExistsException(ExceptionMessages.CINEMA_ALREADY_EXISTS_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.CINEMA_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    public void testHandleProgramAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new ProgramAlreadyExistsException(ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    public void testHandleRoleAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new RoleAlreadyExistsException(ExceptionMessages.ROLE_ALREADY_EXISTS_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.ROLE_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    public void testHandleUserEmailAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new UserEmailAlreadyExistsException(ExceptionMessages.USER_EMAIL_ALREADY_EXISTS_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.USER_EMAIL_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    public void testHandleUsernameAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new UsernameAlreadyExistsException(ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    public void testHandleProjectionNotFoundException_onEndPointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new ProjectionNotFoundException(ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleCategoryNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new CategoryNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleCinemaNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new CinemaNotFoundException(ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleProgramNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new ProgramNotFoundException(ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleUserNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.USER_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleDateNotValidException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.DATE_NOT_VALID_MESSAGE)));
    }

    @Test
    public void testHandleHallNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new HallNotFoundException(ExceptionMessages.HALL_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.HALL_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleItemNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new ItemNotFoundException(ExceptionMessages.ITEM_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.ITEM_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleExistingItemException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new ItemAlreadyExistsException(ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    public void testHandleExistingMovieException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new MovieAlreadyExistsException(ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    public void testHandleOrderNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.ORDER_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleRoleNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.ROLE_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleRoleNotChosenException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new RoleNotChosenException(ExceptionMessages.ROLE_NOT_CHOSEN_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.ROLE_NOT_CHOSEN_MESSAGE)));
    }

    @Test
    public void testHandleMovieNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleDiscountNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new DiscountNotFoundException(ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandeDiscountNotValidException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new DiscountNotValidException(ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE)));
    }

    @Test
    public void testHandleReviewNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new ReviewNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHandleTicketNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new TicketNotFoundException(ExceptionMessages.TICKET_NOT_FOUND_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.TICKET_NOT_FOUND_MESSAGE)));
    }

    @Test
    public void testHallNotAvailableException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new HallNotAvailableException(ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION)));
    }

    @Test
    public void testNoAvailableTicketsException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new NoAvailableTicketsException(ExceptionMessages.NO_AVAILABLE_TICKETS_EXCEPTION));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.NO_AVAILABLE_TICKETS_EXCEPTION)));
    }

    @Test
    public void testNoAvailableItemsException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new NoAvailableItemsException(ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION)));
    }

    @Test
    public void testHandleNotAuthorizedException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isForbidden())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.NOT_AUTHORIZED_MESSAGE)));
    }

    @Test
    public void testHandleNotLoggedInException_onEndpointGetAllCategories_notFound() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new NotLoggedInException(ExceptionMessages.NOT_LOGGED_IN_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.NOT_LOGGED_IN_MESSAGE)));
    }

    @Test
    public void testHandleDiscountAlreadyExistsException_onEndpointGetAllCategories_conflict() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(
            new DiscountAlreadyExistsException(ExceptionMessages.DISCOUNT_ALREADY_EXISTS_MESSAGE));

        mockMvc
            .perform(get(URI))
            .andExpect(status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.DISCOUNT_ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    public void testHandleException_onEndpointGetAllCategories_internalServerError() throws Exception {
        when(categoryService.getAllCategories()).thenThrow(IllegalStateException.class);

        mockMvc
            .perform(get(URI))
            .andExpect(status().isInternalServerError())
            .andExpect(MockMvcResultMatchers.jsonPath(ROOT_ERRORS,
                Matchers.containsInAnyOrder(ExceptionMessages.GLOBAL_EXCEPTION)));
    }
}




