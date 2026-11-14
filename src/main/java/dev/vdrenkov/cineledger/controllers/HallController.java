package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.models.requests.HallRequest;
import dev.vdrenkov.cineledger.services.HallService;
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
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @PostMapping(URIConstants.HALLS_PATH)
    public ResponseEntity<Void> addHall(@RequestBody @Valid HallRequest request) {
        final Hall hall = hallService.addHall(request);

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.HALLS_ID_PATH)
            .buildAndExpand(hall.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.CINEMAS_ID_HALLS_PATH)
    public ResponseEntity<List<HallDto>> getHallsByCinemaId(@PathVariable int id) {
        final List<HallDto> hallDtos = hallService.getHallsByCinemaId(id);

        return ResponseEntity.ok(hallDtos);
    }

    @PutMapping(URIConstants.HALLS_ID_PATH)
    public ResponseEntity<HallDto> updateHall(@RequestBody @Valid HallRequest request, @PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {
        final HallDto hallDto = hallService.updateHall(request, id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(hallDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.HALLS_ID_PATH)
    public ResponseEntity<HallDto> deleteHall(@PathVariable int id, @RequestParam(required = false) Boolean returnOld) {
        final HallDto hallDto = hallService.deleteHall(id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(hallDto) : ResponseEntity.noContent().build();
    }
}
