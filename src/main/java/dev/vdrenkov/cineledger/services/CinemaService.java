package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.CinemaAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CinemaNotFoundException;
import dev.vdrenkov.cineledger.mappers.CinemaMapper;
import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.requests.CinemaRequest;
import dev.vdrenkov.cineledger.repositories.CinemaRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Contains business logic for cinema operations.
 */
@Service
public class CinemaService {
    private static final Logger log = LoggerFactory.getLogger(CinemaService.class);

    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;

    /**
     * Creates a new cinema service with its required collaborators.
     *
     * @param cinemaRepository
     *     cinema repository used by the operation
     * @param cinemaMapper
     *     cinema mapper used by the operation
     */
    @Autowired
    public CinemaService(CinemaRepository cinemaRepository, CinemaMapper cinemaMapper) {
        this.cinemaRepository = cinemaRepository;
        this.cinemaMapper = cinemaMapper;
    }

    /**
     * Creates and persists cinema.
     *
     * @param cinemaRequest
     *     request payload for the cinema operation
     * @return requested cinema value
     */
    public Cinema addCinema(CinemaRequest cinemaRequest) {
        log.info("An attempt to add a new cinema in the database");

        cinemaValidation(cinemaRequest);

        return cinemaRepository.save(new Cinema(cinemaRequest.getAddress(), cinemaRequest.getCity(), 0));
    }

    /**
     * Returns all cinemas matching the supplied criteria.
     *
     * @param city
     *     city used by the operation
     * @param address
     *     address used by the operation
     * @return matching cinema dto values
     */
    public List<CinemaDto> getAllCinemas(String city, String address) {
        if (Objects.isNull(city) && Objects.isNull(address)) {

            log.error("Exception caught: You haven't provided any query parameters ");

            throw new IllegalArgumentException("You haven't provided any query parameters");
        } else if (Objects.nonNull(city) && Objects.nonNull(address)) {

            log.info(String.format("All cinemas with city %s and address %s were requested from the database", city,
                address));

            return cinemaMapper.mapCinemaToCinemaDtoList(cinemaRepository.findAllByCityAndAddress(city, address));
        } else if (Objects.nonNull(city)) {

            log.info(String.format("All cinemas with city %s were requested from the database", city));

            return cinemaMapper.mapCinemaToCinemaDtoList(cinemaRepository.findAllByCity(city));
        } else {

            log.info(String.format("All cinemas with address %s were requested from the database", address));

            return cinemaMapper.mapCinemaToCinemaDtoList(cinemaRepository.findAllByAddress(address));
        }
    }

    /**
     * Returns cinema matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested cinema value
     */
    public Cinema getCinemaById(int id) {
        log.info(String.format("An attempt to extract a cinema with id %d from the database", id));

        return cinemaRepository.findById(id).orElseThrow(() -> {

            log.error(String.format("Exception caught : %s", ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE));

            throw new CinemaNotFoundException(ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns cinema matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return cinema dto result
     */
    public CinemaDto getCinemaDtoById(int id) {
        log.info(String.format("An attempt to extract a cinemaDto with id %d from the database", id));

        return cinemaMapper.mapCinemaToCinemaDto(getCinemaById(id));
    }

    /**
     * Updates cinema and returns the previous state when needed.
     *
     * @param cinemaRequest
     *     request payload for the cinema operation
     * @param id
     *     identifier of the target resource
     * @return cinema dto result
     */
    public CinemaDto updateCinema(CinemaRequest cinemaRequest, int id) {
        final CinemaDto cinemaDto = getCinemaDtoById(id);

        cinemaRepository.save(
            new Cinema(id, cinemaRequest.getAddress(), cinemaRequest.getCity(), cinemaDto.getAverageRating()));

        log.info(String.format("Cinema with an id %d has been updated", id));

        return cinemaDto;
    }

    /**
     * Updates cinema average rating and returns the previous state when needed.
     *
     * @param newRating
     *     replacement rating value
     * @param cinemaId
     *     identifier of the target cinema
     * @return cinema dto result
     */
    public CinemaDto updateCinemaAverageRating(double newRating, int cinemaId) {
        final Cinema cinema = getCinemaById(cinemaId);
        final CinemaDto cinemaDto = cinemaMapper.mapCinemaToCinemaDto(cinema);
        cinema.setAverageRating(newRating);
        cinemaDto.setAverageRating(newRating);

        cinemaRepository.save(cinema);

        log.info(String.format("Updated average rating for cinema with id %d", cinemaId));

        return cinemaDto;
    }

    /**
     * Deletes cinema and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @return cinema dto result
     */
    public CinemaDto deleteCinema(int id) {
        final CinemaDto cinemaDto = getCinemaDtoById(id);

        cinemaRepository.deleteById(id);

        log.info(String.format("Cinema with an id %d has been deleted", id));

        return cinemaDto;
    }

    private void cinemaValidation(CinemaRequest cinemaRequest) {
        cinemaRepository.findByCityAndAddress(cinemaRequest.getCity(), cinemaRequest.getAddress()).ifPresent(cinema -> {
            log.error(String.format("Exception caught: %s", ExceptionMessages.CINEMA_ALREADY_EXISTS_MESSAGE));
            throw new CinemaAlreadyExistsException(ExceptionMessages.CINEMA_ALREADY_EXISTS_MESSAGE);
        });
    }
}


