package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.models.entities.Projection;
import dev.vdrenkov.cineledger.models.requests.ProjectionRequest;
import dev.vdrenkov.cineledger.services.ProjectionService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalTime;
import java.util.List;

/**
 * Exposes REST endpoints for managing projection data.
 */
@RestController
public class ProjectionController {
    private static final Logger log = LoggerFactory.getLogger(ProjectionController.class);

    private final ProjectionService projectionService;

    /**
     * Creates a new projection controller with its required collaborators.
     *
     * @param projectionService
     *     projection service used by the operation
     */
    @Autowired
    public ProjectionController(ProjectionService projectionService) {
        this.projectionService = projectionService;
    }

    /**
     * Creates and persists projection.
     *
     * @param request
     *     request payload containing the submitted data
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.PROJECTIONS_PATH)
    public ResponseEntity<Void> addProjection(@RequestBody @Valid ProjectionRequest request) {
        final Projection projection = this.projectionService.addProjection(request);
        log.info("Request for a projection to be added submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.PROJECTIONS_ID_PATH)
            .buildAndExpand(projection.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Returns projections matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.PROGRAMS_ID_PROJECTIONS_PATH)
    public ResponseEntity<List<ProjectionDto>> getProjectionsByProgramId(@PathVariable int id) {
        final List<ProjectionDto> projectionsDtos = this.projectionService.getProjectionsByProgramId(id);
        log.info("All projections by program id requested from the database");

        return ResponseEntity.ok(projectionsDtos);
    }

    /**
     * Returns projections matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.MOVIES_ID_PROJECTIONS_PATH)
    public ResponseEntity<List<ProjectionDto>> getProjectionsByMovieId(@PathVariable int id) {
        final List<ProjectionDto> projectionsDtos = this.projectionService.getProjectionsByMovieId(id);
        log.info("All projections by movie id requested from the database");

        return ResponseEntity.ok(projectionsDtos);
    }

    /**
     * Returns projections matching the supplied criteria.
     *
     * @param startTime
     *     start time used by the operation
     * @param isBefore
     *     whether to match data before the provided boundary
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.PROJECTIONS_PATH)
    public ResponseEntity<List<ProjectionDto>> getProjectionsByStartTime(
        @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime, @RequestParam boolean isBefore) {

        final List<ProjectionDto> projectionDtos = this.projectionService.getProjectionsByStartTime(startTime,
            isBefore);
        log.info("All projections by start time requested from the database");

        return ResponseEntity.ok(projectionDtos);
    }

    /**
     * Updates projection and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.PROJECTIONS_ID_PATH)
    public ResponseEntity<ProjectionDto> updateProjection(@RequestBody @Valid ProjectionRequest request,
        @PathVariable int id, @RequestParam(required = false) Boolean returnOld) {

        final ProjectionDto projectionDto = this.projectionService.updateProjection(request, id);
        log.info("Projection with id {} updated", id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(projectionDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes projection and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(URIConstants.PROJECTIONS_ID_PATH)
    public ResponseEntity<ProjectionDto> deleteProjection(@PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final ProjectionDto projectionDto = this.projectionService.deleteProjection(id);
        log.info("Projection with id {} deleted", id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(projectionDto) : ResponseEntity.noContent().build();
    }
}



