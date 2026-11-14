package dev.vdrenkov.cineledger.handlers;

import dev.vdrenkov.cineledger.exceptions.*;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.DateTimeException;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
        IllegalArgumentException.class,
        UnsatisfiedServletRequestParameterException.class,
        UnexpectedTypeException.class,
        HttpMessageNotReadableException.class,
        MissingServletRequestParameterException.class,
        MissingPathVariableException.class,
        DateTimeException.class,
        MethodArgumentTypeMismatchException.class,
        HandlerMethodValidationException.class,
        ConstraintViolationException.class
    })
    public ResponseEntity<Map<String, List<String>>> handleInvalidRequest(
            final Exception exception) {
        return error(HttpStatus.BAD_REQUEST, ExceptionMessages.INVALID_REQUEST_MESSAGE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidation(
            final MethodArgumentNotValidException exception) {
        final List<String> errors =
                exception.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
        return ResponseEntity.badRequest().body(Map.of("Errors", errors));
    }

    @ExceptionHandler({
        RoleNotChosenException.class,
        DateNotValidException.class,
        HallNotAvailableException.class,
        NoAvailableTicketsException.class,
        NoAvailableItemsException.class,
        DiscountNotValidException.class
    })
    public ResponseEntity<Map<String, List<String>>> handleBusinessValidation(
            final RuntimeException exception) {
        return error(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({
        CategoryNotFoundException.class,
        CinemaNotFoundException.class,
        DiscountNotFoundException.class,
        HallNotFoundException.class,
        ItemNotFoundException.class,
        MovieNotFoundException.class,
        OrderNotFoundException.class,
        ProgramNotFoundException.class,
        ProjectionNotFoundException.class,
        ReviewNotFoundException.class,
        RoleNotFoundException.class,
        TicketNotFoundException.class,
        UserNotFoundException.class
    })
    public ResponseEntity<Map<String, List<String>>> handleNotFound(
            final RuntimeException exception) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleMissingRoute(
            final NoResourceFoundException exception) {
        return error(HttpStatus.NOT_FOUND, "Resource not found");
    }

    @ExceptionHandler({
        CategoryAlreadyExistsException.class,
        CinemaAlreadyExistsException.class,
        DiscountAlreadyExistsException.class,
        ItemAlreadyExistsException.class,
        MovieAlreadyExistsException.class,
        ProgramAlreadyExistsException.class,
        RoleAlreadyExistsException.class,
        UserEmailAlreadyExistsException.class,
        UsernameAlreadyExistsException.class,
        ResourceInUseException.class
    })
    public ResponseEntity<Map<String, List<String>>> handleConflict(
            final RuntimeException exception) {
        return error(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConcurrencyFailureException.class})
    public ResponseEntity<Map<String, List<String>>> handleDataConflict(
            final RuntimeException exception) {
        return error(HttpStatus.CONFLICT, ExceptionMessages.DATA_CONFLICT_MESSAGE);
    }

    @ExceptionHandler({NotAuthorizedException.class, AccessDeniedException.class})
    public ResponseEntity<Map<String, List<String>>> handleForbidden(
            final RuntimeException exception) {
        return error(HttpStatus.FORBIDDEN, ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
    }

    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity<Map<String, List<String>>> handleNotLoggedIn(
            final NotLoggedInException exception) {
        return error(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, List<String>>> handleBadCredentials(
            final BadCredentialsException exception) {
        return error(HttpStatus.UNAUTHORIZED, ExceptionMessages.AUTHENTICATION_FAILED_MESSAGE);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Map<String, List<String>>> handleAuthenticationFailure(
            final InternalAuthenticationServiceException exception) {
        return error(HttpStatus.BAD_REQUEST, ExceptionMessages.AUTHENTICATION_FAILED_MESSAGE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodNotAllowed(
            final HttpRequestMethodNotSupportedException exception) {
        return error(HttpStatus.METHOD_NOT_ALLOWED, ExceptionMessages.METHOD_NOT_ALLOWED_MESSAGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, List<String>>> handleUnexpectedFailure(
            final Exception exception) {
        log.error("Unexpected request failure", exception);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessages.GLOBAL_EXCEPTION);
    }

    private static ResponseEntity<Map<String, List<String>>> error(
            final HttpStatus status, final String message) {
        return ResponseEntity.status(status).body(Map.of("Errors", List.of(message)));
    }
}
