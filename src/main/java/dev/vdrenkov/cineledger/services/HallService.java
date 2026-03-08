package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.HallNotFoundException;
import dev.vdrenkov.cineledger.mappers.HallMapper;
import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.models.requests.HallRequest;
import dev.vdrenkov.cineledger.repositories.HallRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains business logic for hall operations.
 */
@Service
public class HallService {
    private static final Logger log = LoggerFactory.getLogger(HallService.class);

    private final HallRepository hallRepository;
    private final CinemaService cinemaService;

    /**
     * Creates a new hall service with its required collaborators.
     *
     * @param hallRepository
     *     hall repository used by the operation
     * @param cinemaService
     *     cinema service used by the operation
     */
    @Autowired
    public HallService(HallRepository hallRepository, CinemaService cinemaService) {
        this.hallRepository = hallRepository;
        this.cinemaService = cinemaService;
    }

    /**
     * Creates and persists hall.
     *
     * @param request
     *     request payload containing the submitted data
     * @return requested hall value
     */
    public Hall addHall(HallRequest request) {
        log.info("An attempt to add new hall in the database");

        return hallRepository.save(new Hall(request.getCapacity(), cinemaService.getCinemaById(request.getCinemaId())));
    }

    /**
     * Returns halls matching the supplied criteria.
     *
     * @param cinemaId
     *     identifier of the target cinema
     * @return matching hall dto values
     */
    public List<HallDto> getHallsByCinemaId(int cinemaId) {
        final Cinema cinema = cinemaService.getCinemaById(cinemaId);

        log.info(String.format("All halls with cinema id %d were requested from the database", cinemaId));

        return HallMapper.mapHallListToHallDtoList(hallRepository.findAllByCinemaId(cinema.getId()));
    }

    /**
     * Returns hall matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested hall value
     */
    public Hall getHallById(int id) {
        log.info(String.format("An attempt to extract a hall with an id %d from the database", id));

        return hallRepository.findById(id).orElseThrow(() -> {

            log.error(String.format("Exception caught:  %s", ExceptionMessages.HALL_NOT_FOUND_MESSAGE));

            throw new HallNotFoundException(ExceptionMessages.HALL_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns hall matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return hall dto result
     */
    public HallDto getHallDtoById(int id) {
        log.info(String.format("An attempt to extract a hall DTO with an id %d from the database", id));

        return HallMapper.mapHallToHallDto(getHallById(id));
    }

    /**
     * Updates hall and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @return hall dto result
     */
    public HallDto updateHall(HallRequest request, int id) {
        final HallDto hallDto = getHallDtoById(id);

        hallRepository.save(new Hall(id, request.getCapacity(), cinemaService.getCinemaById(request.getCinemaId())));

        log.info(String.format("Hall with an id %d has been updated", id));

        return hallDto;
    }

    /**
     * Deletes hall and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @return hall dto result
     */
    public HallDto deleteHall(int id) {
        final HallDto hallDto = getHallDtoById(id);

        hallRepository.deleteById(id);

        log.info(String.format("Hall with an id %d has been deleted", id));

        return hallDto;
    }
}


