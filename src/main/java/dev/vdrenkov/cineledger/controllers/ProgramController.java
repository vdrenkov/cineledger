package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.models.entities.Program;
import dev.vdrenkov.cineledger.models.requests.ProgramRequest;
import dev.vdrenkov.cineledger.services.ProgramService;
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
import java.time.LocalDate;
import java.util.List;

/**
 * Exposes REST endpoints for managing program data.
 */
@RestController
public class ProgramController {
    private static final Logger log = LoggerFactory.getLogger(ProgramController.class);
    private final ProgramService programService;

    /**
     * Creates a new program controller with its required collaborators.
     *
     * @param programService
     *     program service used by the operation
     */
    @Autowired
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    /**
     * Creates and persists program.
     *
     * @param programRequest
     *     request payload for the program operation
     * @return HTTP response describing the operation result
     */
    @PostMapping(value = URIConstants.PROGRAMS_PATH)
    public ResponseEntity<Void> addProgram(@RequestBody @Valid ProgramRequest programRequest) {
        final Program program = programService.addProgram(programRequest);
        log.info("A request for a program to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.PROGRAMS_ID_PATH)
            .buildAndExpand(program.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Returns programs matching the supplied criteria.
     *
     * @param value
     *     value used by the operation
     * @param date
     *     date used by the operation
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.PROGRAMS_PATH)
    public ResponseEntity<List<ProgramDto>> getProgramsByDate(
        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        final List<ProgramDto> programs = programService.getAllPrograms(date);
        log.info("All programs by date were requested from the database");

        return ResponseEntity.ok(programs);
    }

    /**
     * Returns programs matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return HTTP response describing the operation result
     */
    @GetMapping(value = URIConstants.CINEMAS_ID_PROGRAMS_PATH)
    public ResponseEntity<List<ProgramDto>> getProgramsByCinemaId(@PathVariable int id) {
        final List<ProgramDto> programs = programService.getProgramsByCinemaId(id);
        log.info("Program by cinema id was requested from the database");

        return ResponseEntity.ok(programs);
    }

    /**
     * Updates program and returns the previous state when needed.
     *
     * @param programRequest
     *     request payload for the program operation
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(value = URIConstants.PROGRAMS_ID_PATH)
    public ResponseEntity<ProgramDto> updateProgram(@RequestBody @Valid ProgramRequest programRequest,
        @PathVariable int id, @RequestParam(required = false) boolean returnOld) {

        final ProgramDto programDto = programService.updateProgram(programRequest, id);
        log.info(String.format("Program with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(programDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes program and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(value = URIConstants.PROGRAMS_ID_PATH)
    public ResponseEntity<ProgramDto> deleteProgram(@PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        final ProgramDto programDto = programService.deleteProgram(id);
        log.info(String.format("Program with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(programDto) : ResponseEntity.noContent().build();
    }
}



