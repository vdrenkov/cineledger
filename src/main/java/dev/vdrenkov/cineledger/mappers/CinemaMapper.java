package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps cinema domain models to DTO representations used by the API.
 */
@Component
public class CinemaMapper {
    private static final Logger log = LoggerFactory.getLogger(CinemaMapper.class);

    /**
     * Maps cinema values to cinema dto values.
     *
     * @param cinema
     *     cinema entity to transform
     * @return cinema dto result
     */
    public CinemaDto mapCinemaToCinemaDto(Cinema cinema) {
        log.info(String.format("The cinema with an id %d is being mapped to a cinema DTO", cinema.getId()));
        return new CinemaDto(cinema.getId(), cinema.getAddress(), cinema.getCity(), cinema.getAverageRating());
    }

    /**
     * Maps cinema values to cinema dto list values.
     *
     * @param cinemaList
     *     cinema list used by the operation
     * @return matching cinema dto values
     */
    public List<CinemaDto> mapCinemaToCinemaDtoList(List<Cinema> cinemaList) {
        return cinemaList.stream().map(this::mapCinemaToCinemaDto).collect(Collectors.toList());
    }
}

