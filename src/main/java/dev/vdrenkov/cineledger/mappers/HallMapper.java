package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Hall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps hall domain models to DTO representations used by the API.
 */
public final class HallMapper {
    private static final Logger log = LoggerFactory.getLogger(HallMapper.class);

    private HallMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps hall values to hall dto values.
     *
     * @param hall
     *     hall entity to transform
     * @return hall dto result
     */
    public static HallDto mapHallToHallDto(Hall hall) {
        log.info("Mapping the hall with an id {} to a hall DTO", hall.getId());
        return new HallDto(hall.getId(), hall.getCapacity(), CinemaMapper.mapCinemaToCinemaDto(hall.getCinema()));
    }

    /**
     * Maps hall list values to hall dto list values.
     *
     * @param halls
     *     hall entities to transform
     * @return matching hall dto values
     */
    public static List<HallDto> mapHallListToHallDtoList(List<Hall> halls) {
        return halls.stream().map(HallMapper::mapHallToHallDto).toList();
    }
}

