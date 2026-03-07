package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.requests.RoleRequest;
import dev.vdrenkov.cineledger.services.RoleService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Exposes REST endpoints for managing role data.
 */
@RestController
public class RoleController {
    private static final Logger log = LoggerFactory.getLogger(RoleController.class);

    private final RoleService roleService;

    /**
     * Creates a new role controller with its required collaborators.
     *
     * @param roleService
     *     role service used by the operation
     */
    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Creates and persists role.
     *
     * @param request
     *     request payload containing the submitted data
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.ROLES_PATH)
    public ResponseEntity<Void> addRole(@RequestBody @Valid RoleRequest request) {
        final Role role = roleService.addRole(request);
        log.info("A request for a user role to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.ROLES_ID_PATH)
            .buildAndExpand(role.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Returns all roles matching the supplied criteria.
     *
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.ROLES_PATH)
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        final List<RoleDto> roleDtos = roleService.getAllRolesDto();
        log.info("All user roles were requested from the database");

        return ResponseEntity.ok(roleDtos);
    }

    /**
     * Returns role matching the supplied criteria.
     *
     * @param name
     *     name used by the operation
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.ROLES_PATH, params = "name")
    public ResponseEntity<RoleDto> getRoleByName(@RequestParam String name) {
        final RoleDto roleDto = roleService.getRoleDtoByName(name);
        log.info("User role by name was requested from the database");

        return ResponseEntity.ok(roleDto);
    }

    /**
     * Updates role and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.ROLES_ID_PATH)
    public ResponseEntity<RoleDto> updateRole(@RequestBody @Valid RoleRequest request, @PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        final RoleDto roleDto = roleService.updateRole(request, id);
        log.info(String.format("User role with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(roleDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes role and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(URIConstants.ROLES_ID_PATH)
    public ResponseEntity<RoleDto> deleteRole(@PathVariable int id, @RequestParam(required = false) boolean returnOld) {
        final RoleDto roleDto = roleService.deleteRole(id);
        log.info(String.format("User role with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(roleDto) : ResponseEntity.noContent().build();
    }
}


