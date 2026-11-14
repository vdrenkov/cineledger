package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.RoleDto;
import dev.vdrenkov.cineledger.models.entities.Role;
import dev.vdrenkov.cineledger.models.requests.RoleRequest;
import dev.vdrenkov.cineledger.services.RoleService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;

import jakarta.validation.Valid;

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

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(URIConstants.ROLES_PATH)
    public ResponseEntity<Void> addRole(@RequestBody @Valid RoleRequest request) {
        final Role role = roleService.addRole(request);

        URI location =
                UriComponentsBuilder.fromUriString(URIConstants.ROLES_ID_PATH)
                        .buildAndExpand(role.getId())
                        .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.ROLES_PATH)
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        final List<RoleDto> roleDtos = roleService.getAllRolesDto();

        return ResponseEntity.ok(roleDtos);
    }

    @GetMapping(value = URIConstants.ROLES_PATH, params = "name")
    public ResponseEntity<RoleDto> getRoleByName(@RequestParam String name) {
        final RoleDto roleDto = roleService.getRoleDtoByName(name);

        return ResponseEntity.ok(roleDto);
    }

    @PutMapping(URIConstants.ROLES_ID_PATH)
    public ResponseEntity<RoleDto> updateRole(
            @RequestBody @Valid RoleRequest request,
            @PathVariable int id,
            @RequestParam(required = false) Boolean returnOld) {

        final RoleDto roleDto = roleService.updateRole(request, id);

        return Boolean.TRUE.equals(returnOld)
                ? ResponseEntity.ok(roleDto)
                : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.ROLES_ID_PATH)
    public ResponseEntity<RoleDto> deleteRole(
            @PathVariable int id, @RequestParam(required = false) Boolean returnOld) {
        final RoleDto roleDto = roleService.deleteRole(id);

        return Boolean.TRUE.equals(returnOld)
                ? ResponseEntity.ok(roleDto)
                : ResponseEntity.noContent().build();
    }
}
