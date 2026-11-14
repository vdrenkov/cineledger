package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.ProjectionDto;
import dev.vdrenkov.cineledger.models.entities.Projection;
import dev.vdrenkov.cineledger.models.requests.ProjectionRequest;
import dev.vdrenkov.cineledger.services.ProjectionService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
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

@RestController
public class ProjectionController {

    private final ProjectionService projectionService;

    public ProjectionController(ProjectionService projectionService) {
        this.projectionService = projectionService;
    }

    @PostMapping(URIConstants.PROJECTIONS_PATH)
    public ResponseEntity<Void> addProjection(@RequestBody @Valid ProjectionRequest request) {
        final Projection projection = this.projectionService.addProjection(request);

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.PROJECTIONS_ID_PATH)
            .buildAndExpand(projection.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.PROGRAMS_ID_PROJECTIONS_PATH)
    public ResponseEntity<List<ProjectionDto>> getProjectionsByProgramId(@PathVariable int id) {
        final List<ProjectionDto> projectionsDtos = this.projectionService.getProjectionsByProgramId(id);

        return ResponseEntity.ok(projectionsDtos);
    }

    @GetMapping(URIConstants.MOVIES_ID_PROJECTIONS_PATH)
    public ResponseEntity<List<ProjectionDto>> getProjectionsByMovieId(@PathVariable int id) {
        final List<ProjectionDto> projectionsDtos = this.projectionService.getProjectionsByMovieId(id);

        return ResponseEntity.ok(projectionsDtos);
    }

    @GetMapping(URIConstants.PROJECTIONS_PATH)
    public ResponseEntity<List<ProjectionDto>> getProjectionsByStartTime(
        @RequestParam @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime, @RequestParam boolean isBefore) {

        final List<ProjectionDto> projectionDtos = this.projectionService.getProjectionsByStartTime(startTime,
            isBefore);

        return ResponseEntity.ok(projectionDtos);
    }

    @PutMapping(URIConstants.PROJECTIONS_ID_PATH)
    public ResponseEntity<ProjectionDto> updateProjection(@RequestBody @Valid ProjectionRequest request,
        @PathVariable int id, @RequestParam(required = false) Boolean returnOld) {

        final ProjectionDto projectionDto = this.projectionService.updateProjection(request, id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(projectionDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.PROJECTIONS_ID_PATH)
    public ResponseEntity<ProjectionDto> deleteProjection(@PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final ProjectionDto projectionDto = this.projectionService.deleteProjection(id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(projectionDto) : ResponseEntity.noContent().build();
    }
}
