package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.requests.CinemaRequest;
import dev.vdrenkov.cineledger.services.CinemaService;
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
 * Exposes REST endpoints for managing cinema data.
 */
@RestController
public class CinemaController {
    private static final Logger log = LoggerFactory.getLogger(CinemaController.class);

    private final CinemaService cinemaService;

    /**
     * Creates a new cinema controller with its required collaborators.
     *
     * @param cinemaService
     *     cinema service used by the operation
     */
    @Autowired
    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    /**
     * Returns cinemas matching the supplied criteria.
     *
     * @param city
     *     city used by the operation
     * @param address
     *     address used by the operation
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.CINEMAS_PATH)
    public ResponseEntity<List<CinemaDto>> getCinemas(@RequestParam(required = false) String city,
        @RequestParam(required = false) String address) {

        final List<CinemaDto> cinemaDtos = cinemaService.getAllCinemas(city, address);
        log.info("All cinemas requested from the database");

        return ResponseEntity.ok(cinemaDtos);
    }

    /**
     * Creates and persists cinema.
     *
     * @param request
     *     request payload containing the submitted data
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.CINEMAS_PATH)
    public ResponseEntity<Void> addCinema(@RequestBody @Valid CinemaRequest request) {
        final Cinema cinema = cinemaService.addCinema(request);
        log.info("Request for a cinema to be added submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.CINEMAS_ID_PATH)
            .buildAndExpand(cinema.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Updates cinema and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.CINEMAS_ID_PATH)
    public ResponseEntity<CinemaDto> updateCinema(@RequestBody @Valid CinemaRequest request, @PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final CinemaDto cinemaDto = cinemaService.updateCinema(request, id);
        log.info("Cinema with id {} updated", id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(cinemaDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes cinema and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(URIConstants.CINEMAS_ID_PATH)
    public ResponseEntity<CinemaDto> deleteCinema(@PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final CinemaDto cinemaDto = cinemaService.deleteCinema(id);
        log.info("Cinema with id {} deleted", id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(cinemaDto) : ResponseEntity.noContent().build();
    }
}



