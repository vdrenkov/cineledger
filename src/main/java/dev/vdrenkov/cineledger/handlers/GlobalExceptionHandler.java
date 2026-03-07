package dev.vdrenkov.cineledger.handlers;

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
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import jakarta.validation.UnexpectedTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converts application and framework exceptions into consistent HTTP error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Builds an HTTP error response for illegal argument failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, List<String>>> handleIllegalArgumentException(
        IllegalArgumentException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.INVALID_REQUEST_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for unsatisfied servlet request parameter failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    public ResponseEntity<Map<String, List<String>>> handleUnsatisfiedServletRequestParameterException(
        UnsatisfiedServletRequestParameterException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.INVALID_REQUEST_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for null pointer failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, List<String>>> handleNullPointerException(NullPointerException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.GLOBAL_EXCEPTION);

        return new ResponseEntity<>(errorsMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Builds an HTTP error response for unexpected type failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Map<String, List<String>>> handleUnexpectedTypeException(UnexpectedTypeException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.INVALID_REQUEST_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for http message not readable failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, List<String>>> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.INVALID_REQUEST_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for http request method not supported failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, List<String>>> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.METHOD_NOT_ALLOWED_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Builds an HTTP error response for missing servlet request parameter failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, List<String>>> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.INVALID_REQUEST_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for missing path variable failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Map<String, List<String>>> handleMissingPathVariableException(
        MissingPathVariableException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.INVALID_REQUEST_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for date time parse failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Map<String, List<String>>> handleDateTimeParseException(DateTimeParseException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.INVALID_REQUEST_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for controller validation failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleControllerValidationException(
        MethodArgumentNotValidException exception) {

        log.error("Caught exception: ", exception);

        List<String> errors = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        return new ResponseEntity<>(formatErrorsResponse(errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for method argument type mismatch failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.INVALID_REQUEST_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for internal authentication service failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Map<String, List<String>>> handleInternalAuthenticationServiceException(
        InternalAuthenticationServiceException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(
            ExceptionMessages.AUTHENTICATION_FAILED_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for role not chosen failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(RoleNotChosenException.class)
    public ResponseEntity<Map<String, List<String>>> handleRoleNotChosenException(RoleNotChosenException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for bad credentials failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, List<String>>> handleBadCredentialsException(BadCredentialsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(
            ExceptionMessages.AUTHENTICATION_FAILED_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Builds an HTTP error response for data integrity violation failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, List<String>>> handleDataIntegrityViolationException(
        DataIntegrityViolationException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.DATA_CONFLICT_MESSAGE);

        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for date not valid failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(DateNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleDateNotValidException(DateNotValidException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for not logged in failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity<Map<String, List<String>>> handleNotLoggedInException(NotLoggedInException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Builds an HTTP error response for not authorized failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Map<String, List<String>>> handleNotAuthorizedException(NotAuthorizedException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.FORBIDDEN);
    }

    /**
     * Builds an HTTP error response for projection not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(ProjectionNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleProjectionNotFoundException(
        ProjectionNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for category not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleCategoryNotFoundException(
        CategoryNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for cinema not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(CinemaNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleCinemaNotFoundException(CinemaNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for program not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(ProgramNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleProgramNotFoundException(
        ProgramNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for user not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleUserNotFoundException(UserNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for hall not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(HallNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleHallNotFoundException(HallNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for item not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleItemNotFoundException(ItemNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for order not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleOrderNotFoundException(OrderNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for movie not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleMovieNotFoundException(MovieNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for role not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleRoleNotFoundException(RoleNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for ticket not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleTicketNotFoundException(TicketNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for discount not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(DiscountNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleDiscountNotFoundException(
        DiscountNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for review not found failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleReviewNotFoundException(ReviewNotFoundException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
    }

    /**
     * Builds an HTTP error response for hall not available failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(HallNotAvailableException.class)
    public ResponseEntity<Map<String, List<String>>> handleHallNotAvailableException(
        HallNotAvailableException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for no available tickets failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(NoAvailableTicketsException.class)
    public ResponseEntity<Map<String, List<String>>> handleNoAvailableTicketsException(
        NoAvailableTicketsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for no available items failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(NoAvailableItemsException.class)
    public ResponseEntity<Map<String, List<String>>> handleNoAvailableItemsException(
        NoAvailableItemsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for category already exists failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleCategoryAlreadyExistsException(
        CategoryAlreadyExistsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for cinema already exists failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(CinemaAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleCinemaAlreadyExistsException(
        CinemaAlreadyExistsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for program already exists failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(ProgramAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleProgramAlreadyExistsException(
        ProgramAlreadyExistsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for role already exists failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleRoleAlreadyExistsException(
        RoleAlreadyExistsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for user email already exists failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleUserEmailAlreadyExistsException(
        UserEmailAlreadyExistsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for username already exists failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleUsernameAlreadyExistsException(
        UsernameAlreadyExistsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for existing item failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleExistingItemException(ItemAlreadyExistsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for existing movie failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(MovieAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleExistingMovieException(
        MovieAlreadyExistsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for discount not valid failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(DiscountNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleDiscountNotValidException(
        DiscountNotValidException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Builds an HTTP error response for discount already exists failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(DiscountAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleDiscountAlreadyExistsException(
        DiscountAlreadyExistsException exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

        return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
    }

    /**
     * Builds an HTTP error response for  failures.
     *
     * @param exception
     *     raised exception instance
     * @return HTTP response describing the operation result
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, List<String>>> handleException(Exception exception) {
        log.error(ExceptionMessages.CAUGHT_EXCEPTION, exception);

        final Map<String, List<String>> errorsMap = formatErrorsResponse(ExceptionMessages.GLOBAL_EXCEPTION);

        return new ResponseEntity<>(errorsMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, List<String>> formatErrorsResponse(String... errors) {
        return formatErrorsResponse(Arrays.stream(errors).collect(Collectors.toList()));
    }

    private Map<String, List<String>> formatErrorsResponse(List<String> errors) {
        final Map<String, List<String>> errorResponse = new HashMap<>(4);
        errorResponse.put("Errors", errors);
        return errorResponse;
    }
}



