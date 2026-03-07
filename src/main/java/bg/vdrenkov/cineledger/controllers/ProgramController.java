package bg.vdrenkov.cineledger.controllers;

import bg.vdrenkov.cineledger.models.dtos.ProgramDto;
import bg.vdrenkov.cineledger.models.entities.Program;
import bg.vdrenkov.cineledger.models.requests.ProgramRequest;
import bg.vdrenkov.cineledger.services.ProgramService;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
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

@RestController
public class ProgramController {

    private static final Logger log = LoggerFactory.getLogger(ProgramController.class);
    private final ProgramService programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping(value = URIConstants.PROGRAMS_PATH)
    public ResponseEntity<Void> addProgram(@RequestBody @Valid ProgramRequest programRequest) {
        Program program = programService.addProgram(programRequest);
        log.info("A request for a program to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.PROGRAMS_ID_PATH)
            .buildAndExpand(program.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = URIConstants.PROGRAMS_PATH)
    public ResponseEntity<List<ProgramDto>> getProgramsByDate(
        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        List<ProgramDto> programs = programService.getAllPrograms(date);
        log.info("All programs by date were requested from the database");

        return ResponseEntity.ok(programs);
    }

    @GetMapping(value = URIConstants.CINEMAS_ID_PROGRAMS_PATH)
    public ResponseEntity<List<ProgramDto>> getProgramsByCinemaId(@PathVariable int id) {
        List<ProgramDto> programs = programService.getProgramsByCinemaId(id);
        log.info("Program by cinema id was requested from the database");

        return ResponseEntity.ok(programs);
    }

    @PutMapping(value = URIConstants.PROGRAMS_ID_PATH)
    public ResponseEntity<ProgramDto> updateProgram(@RequestBody @Valid ProgramRequest programRequest,
        @PathVariable int id, @RequestParam(required = false) boolean returnOld) {

        ProgramDto programDto = programService.updateProgram(programRequest, id);
        log.info(String.format("Program with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(programDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = URIConstants.PROGRAMS_ID_PATH)
    public ResponseEntity<ProgramDto> deleteProgram(@PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        ProgramDto programDto = programService.deleteProgram(id);
        log.info(String.format("Program with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(programDto) : ResponseEntity.noContent().build();
    }
}



