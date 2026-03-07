package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.requests.AdminRequest;
import dev.vdrenkov.cineledger.models.requests.LoginRequest;
import dev.vdrenkov.cineledger.models.requests.UserRequest;
import dev.vdrenkov.cineledger.services.UserService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Exposes REST endpoints for managing user data.
 */
@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    /**
     * Creates a new user controller with its required collaborators.
     *
     * @param userService
     *     user service used by the operation
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Authenticates the submitted credentials and returns the session cookie response.
     *
     * @param request
     *     request payload containing the submitted data
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.LOGIN_PATH)
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {
        final HttpCookie cookie = userService.login(request);
        log.info("A login request has been submitted");

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    /**
     * Registers a new end user and returns the authentication cookie response.
     *
     * @param request
     *     request payload containing the submitted data
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.REGISTRATION_PATH)
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRequest request) {
        final HttpCookie cookie = userService.registerUser(request);
        log.info("A registration request has been submitted");

        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    /**
     * Creates a user account on behalf of an administrator.
     *
     * @param request
     *     request payload containing the submitted data
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.ADMINS_PATH)
    public ResponseEntity<Void> registerUserByAdmin(@RequestBody @Valid AdminRequest request) {
        userService.registerUserByAdmin(request);
        log.info("A registration request by an administrator has been submitted");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Returns user matching the supplied criteria.
     *
     * @param username
     *     username to search for
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.USERS_PATH, params = "username")
    public ResponseEntity<UserDto> getUserByUsername(@RequestParam String username) {
        final UserDto userDto = userService.getUserDtoByUsername(username);
        log.info("User by username was requested from the database");

        return ResponseEntity.ok(userDto);
    }

    /**
     * Returns user matching the supplied criteria.
     *
     * @param email
     *     email address to search for
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.USERS_PATH, params = "email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        final UserDto userDto = userService.getUserDtoByEmail(email);
        log.info("User by email was requested from the database");

        return ResponseEntity.ok(userDto);
    }

    /**
     * Returns users matching the supplied criteria.
     *
     * @param roleName
     *     role name to search for
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.USERS_PATH, params = "roleName")
    public ResponseEntity<List<UserDto>> getUsersByRoleName(@RequestParam String roleName) {
        final List<UserDto> userDtos = userService.getUsersDtoByRoleName(roleName);
        log.info("Users by role name were requested from the database");

        return ResponseEntity.ok(userDtos);
    }

    /**
     * Returns users matching the supplied criteria.
     *
     * @param joinDate
     *     join date boundary to filter by
     * @param isBefore
     *     whether to match data before the provided boundary
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.USERS_PATH, params = { "joinDate", "isBefore" })
    public ResponseEntity<List<UserDto>> getUsersByJoinDate(
        @RequestParam("joinDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate joinDate,
        @RequestParam boolean isBefore) {

        final List<UserDto> userDtos = userService.getUsersDtosByJoinDate(joinDate, isBefore);
        log.info("All Users by join date were requested from the database");

        return ResponseEntity.ok(userDtos);
    }

    /**
     * Updates user and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.USERS_ID_PATH)
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserRequest request, @PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        final UserDto userDto = userService.updateUser(request, id);
        log.info(String.format("User with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(userDto) : ResponseEntity.noContent().build();
    }

    /**
     * Updates user and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.ADMINS_ID_PATH)
    public ResponseEntity<UserDto> updateUserByAdmin(@RequestBody @Valid AdminRequest request, @PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        final UserDto userDto = userService.updateUserByAdmin(request, id);
        log.info(String.format("User with id %d was updated by an admin", id));

        return returnOld ? ResponseEntity.ok(userDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes user and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(URIConstants.USERS_ID_PATH)
    public ResponseEntity<UserDto> deleteUser(@PathVariable int id, @RequestParam(required = false) boolean returnOld) {
        final UserDto userDto = userService.deleteUser(id);
        log.info(String.format("User with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(userDto) : ResponseEntity.noContent().build();
    }

    /**
     * Triggers password recovery for the requested user.
     *
     * @param username
     *     username to search for
     * @return HTTP response describing the operation result
     */
    @PatchMapping("/password-recovery")
    public ResponseEntity<Void> recoverPassword(@RequestParam String username) {
        userService.recoverPassword(username);
        log.info(String.format("A password recovery request has been submitted for user %s ", username));
        return ResponseEntity.ok().build();
    }
}


