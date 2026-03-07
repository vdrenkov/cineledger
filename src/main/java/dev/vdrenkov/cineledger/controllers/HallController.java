package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.models.requests.HallRequest;
import dev.vdrenkov.cineledger.services.HallService;
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
 * Exposes REST endpoints for managing hall data.
 */
@RestController
public class HallController {

    private static final Logger log = LoggerFactory.getLogger(HallController.class);

    private final HallService hallService;

    /**
     * Creates a new hall controller with its required collaborators.
     *
     * @param hallService
     *     hall service used by the operation
     */
    @Autowired
    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    /**
     * Creates and persists hall.
     *
     * @param request
     *     request payload containing the submitted data
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.HALLS_PATH)
    public ResponseEntity<Void> addHall(@RequestBody @Valid HallRequest request) {
        final Hall hall = hallService.addHall(request);
        log.info("A request for a hall to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.HALLS_ID_PATH)
            .buildAndExpand(hall.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Returns halls matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.CINEMAS_ID_HALLS_PATH)
    public ResponseEntity<List<HallDto>> getHallsByCinemaId(@PathVariable int id) {
        final List<HallDto> hallDtos = hallService.getHallsByCinemaId(id);
        log.info(String.format("All halls with cinema id %d were requested from the database", id));

        return ResponseEntity.ok(hallDtos);
    }

    /**
     * Updates hall and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.HALLS_ID_PATH)
    public ResponseEntity<HallDto> updateHall(@RequestBody @Valid HallRequest request, @PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {
        final HallDto hallDto = hallService.updateHall(request, id);
        log.info(String.format("Hall with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(hallDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes hall and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(URIConstants.HALLS_ID_PATH)
    public ResponseEntity<HallDto> deleteHall(@PathVariable int id, @RequestParam(required = false) boolean returnOld) {
        final HallDto hallDto = hallService.deleteHall(id);
        log.info(String.format("Hall with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(hallDto) : ResponseEntity.noContent().build();
    }
}


