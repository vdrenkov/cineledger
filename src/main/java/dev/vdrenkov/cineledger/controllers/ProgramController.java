package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.models.entities.Program;
import dev.vdrenkov.cineledger.models.requests.ProgramRequest;
import dev.vdrenkov.cineledger.services.ProgramService;
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
import java.time.LocalDate;
import java.util.List;

@RestController
public class ProgramController {
    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping(URIConstants.PROGRAMS_PATH)
    public ResponseEntity<Void> addProgram(@RequestBody @Valid ProgramRequest programRequest) {
        final Program program = programService.addProgram(programRequest);

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.PROGRAMS_ID_PATH)
            .buildAndExpand(program.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.PROGRAMS_PATH)
    public ResponseEntity<List<ProgramDto>> getProgramsByDate(
        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        final List<ProgramDto> programs = programService.getAllPrograms(date);

        return ResponseEntity.ok(programs);
    }

    @GetMapping(URIConstants.CINEMAS_ID_PROGRAMS_PATH)
    public ResponseEntity<List<ProgramDto>> getProgramsByCinemaId(@PathVariable int id) {
        final List<ProgramDto> programs = programService.getProgramsByCinemaId(id);

        return ResponseEntity.ok(programs);
    }

    @PutMapping(URIConstants.PROGRAMS_ID_PATH)
    public ResponseEntity<ProgramDto> updateProgram(@RequestBody @Valid ProgramRequest programRequest,
        @PathVariable int id, @RequestParam(required = false) Boolean returnOld) {

        final ProgramDto programDto = programService.updateProgram(programRequest, id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(programDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.PROGRAMS_ID_PATH)
    public ResponseEntity<ProgramDto> deleteProgram(@PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final ProgramDto programDto = programService.deleteProgram(id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(programDto) : ResponseEntity.noContent().build();
    }
}
