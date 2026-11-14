package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.requests.CinemaRequest;
import dev.vdrenkov.cineledger.services.CinemaService;
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
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping(URIConstants.CINEMAS_PATH)
    public ResponseEntity<List<CinemaDto>> getCinemas(@RequestParam(required = false) String city,
        @RequestParam(required = false) String address) {

        final List<CinemaDto> cinemaDtos = cinemaService.getAllCinemas(city, address);

        return ResponseEntity.ok(cinemaDtos);
    }

    @PostMapping(URIConstants.CINEMAS_PATH)
    public ResponseEntity<Void> addCinema(@RequestBody @Valid CinemaRequest request) {
        final Cinema cinema = cinemaService.addCinema(request);

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.CINEMAS_ID_PATH)
            .buildAndExpand(cinema.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(URIConstants.CINEMAS_ID_PATH)
    public ResponseEntity<CinemaDto> updateCinema(@RequestBody @Valid CinemaRequest request, @PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final CinemaDto cinemaDto = cinemaService.updateCinema(request, id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(cinemaDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.CINEMAS_ID_PATH)
    public ResponseEntity<CinemaDto> deleteCinema(@PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final CinemaDto cinemaDto = cinemaService.deleteCinema(id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(cinemaDto) : ResponseEntity.noContent().build();
    }
}
