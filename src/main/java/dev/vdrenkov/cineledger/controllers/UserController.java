package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.requests.AdminRequest;
import dev.vdrenkov.cineledger.models.requests.LoginRequest;
import dev.vdrenkov.cineledger.models.requests.UserRequest;
import dev.vdrenkov.cineledger.services.UserService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
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

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(URIConstants.LOGIN_PATH)
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {
        final HttpCookie cookie = userService.login(request);

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping(URIConstants.REGISTRATION_PATH)
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRequest request) {
        final HttpCookie cookie = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping(URIConstants.ADMINS_PATH)
    public ResponseEntity<Void> registerUserByAdmin(@RequestBody @Valid AdminRequest request) {
        userService.registerUserByAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = URIConstants.USERS_PATH, params = "username")
    public ResponseEntity<UserDto> getUserByUsername(@RequestParam String username) {
        final UserDto userDto = userService.getUserDtoByUsername(username);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping(value = URIConstants.USERS_PATH, params = "email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        final UserDto userDto = userService.getUserDtoByEmail(email);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping(value = URIConstants.USERS_PATH, params = "roleName")
    public ResponseEntity<List<UserDto>> getUsersByRoleName(@RequestParam String roleName) {
        final List<UserDto> userDtos = userService.getUsersDtoByRoleName(roleName);

        return ResponseEntity.ok(userDtos);
    }

    @GetMapping(value = URIConstants.USERS_PATH, params = { "joinDate", "isBefore" })
    public ResponseEntity<List<UserDto>> getUsersByJoinDate(
        @RequestParam("joinDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate joinDate,
        @RequestParam boolean isBefore) {

        final List<UserDto> userDtos = userService.getUsersDtosByJoinDate(joinDate, isBefore);

        return ResponseEntity.ok(userDtos);
    }

    @PutMapping(URIConstants.USERS_ID_PATH)
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserRequest request, @PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final UserDto userDto = userService.updateUser(request, id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(userDto) : ResponseEntity.noContent().build();
    }

    @PutMapping(URIConstants.ADMINS_ID_PATH)
    public ResponseEntity<UserDto> updateUserByAdmin(@RequestBody @Valid AdminRequest request, @PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final UserDto userDto = userService.updateUserByAdmin(request, id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(userDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.USERS_ID_PATH)
    public ResponseEntity<UserDto> deleteUser(@PathVariable int id, @RequestParam(required = false) Boolean returnOld) {
        final UserDto userDto = userService.deleteUser(id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(userDto) : ResponseEntity.noContent().build();
    }

    @PatchMapping("/password-recovery")
    public ResponseEntity<Void> recoverPassword(@RequestParam String username) {
        userService.recoverPassword(username);

        return ResponseEntity.ok().build();
    }
}
