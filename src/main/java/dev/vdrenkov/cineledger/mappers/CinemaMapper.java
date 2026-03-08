package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps cinema domain models to DTO representations used by the API.
 */
public final class CinemaMapper {
    private static final Logger log = LoggerFactory.getLogger(CinemaMapper.class);

    private CinemaMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps cinema values to cinema dto values.
     *
     * @param cinema
     *     cinema entity to transform
     * @return cinema dto result
     */
    public static CinemaDto mapCinemaToCinemaDto(Cinema cinema) {
        log.info("Mapping the cinema with an id {} to a cinema DTO", cinema.getId());
        return new CinemaDto(cinema.getId(), cinema.getAddress(), cinema.getCity(), cinema.getAverageRating());
    }

    /**
     * Maps cinema values to cinema dto list values.
     *
     * @param cinemaList
     *     cinema list used by the operation
     * @return matching cinema dto values
     */
    public static List<CinemaDto> mapCinemaToCinemaDtoList(List<Cinema> cinemaList) {
        return cinemaList.stream().map(CinemaMapper::mapCinemaToCinemaDto).toList();
    }
}

