package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Hall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps hall domain models to DTO representations used by the API.
 */
@Component
public class HallMapper {

    private static final Logger log = LoggerFactory.getLogger(HallMapper.class);

    private final CinemaMapper cinemaMapper;

    /**
     * Creates a new hall mapper with its required collaborators.
     *
     * @param cinemaMapper
     *     cinema mapper used by the operation
     */
    @Autowired
    public HallMapper(CinemaMapper cinemaMapper) {
        this.cinemaMapper = cinemaMapper;
    }

    /**
     * Maps hall values to hall dto values.
     *
     * @param hall
     *     hall entity to transform
     * @return hall dto result
     */
    public HallDto mapHallToHallDto(Hall hall) {
        log.info(String.format("The hall with an id %d is being mapped to a hall DTO", hall.getId()));
        return new HallDto(hall.getId(), hall.getCapacity(), cinemaMapper.mapCinemaToCinemaDto(hall.getCinema()));
    }

    /**
     * Maps hall list values to hall dto list values.
     *
     * @param halls
     *     hall entities to transform
     * @return matching hall dto values
     */
    public List<HallDto> mapHallListToHallDtoList(List<Hall> halls) {
        return halls.stream().map(this::mapHallToHallDto).collect(Collectors.toList());
    }
}

