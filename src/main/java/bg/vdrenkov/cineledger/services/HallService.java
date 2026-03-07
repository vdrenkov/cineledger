package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.HallNotFoundException;
import bg.vdrenkov.cineledger.mappers.HallMapper;
import bg.vdrenkov.cineledger.models.dtos.HallDto;
import bg.vdrenkov.cineledger.models.entities.Cinema;
import bg.vdrenkov.cineledger.models.entities.Hall;
import bg.vdrenkov.cineledger.models.requests.HallRequest;
import bg.vdrenkov.cineledger.repositories.HallRepository;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallService {

    private static final Logger log = LoggerFactory.getLogger(HallService.class);

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;
    private final CinemaService cinemaService;

    @Autowired
    public HallService(HallRepository hallRepository, HallMapper hallMapper, CinemaService cinemaService) {
        this.hallRepository = hallRepository;
        this.hallMapper = hallMapper;
        this.cinemaService = cinemaService;
    }

    public Hall addHall(HallRequest request) {
        log.info("An attempt to add new hall in the database");

        return hallRepository.save(new Hall(request.getCapacity(), cinemaService.getCinemaById(request.getCinemaId())));
    }

    public List<HallDto> getHallsByCinemaId(int cinemaId) {
        Cinema cinema = cinemaService.getCinemaById(cinemaId);

        log.info(String.format("All halls with cinema id %d were requested from the database", cinemaId));

        return hallMapper.mapHallListToHallDtoList(hallRepository.findAllByCinemaId(cinema.getId()));
    }

    public Hall getHallById(int id) {
        log.info(String.format("An attempt to extract a hall with an id %d from the database", id));

        return hallRepository.findById(id).orElseThrow(() -> {

            log.error(String.format("Exception caught:  %s", ExceptionMessages.HALL_NOT_FOUND_MESSAGE));

            throw new HallNotFoundException(ExceptionMessages.HALL_NOT_FOUND_MESSAGE);
        });
    }

    public HallDto getHallDtoById(int id) {
        log.info(String.format("An attempt to extract a hall DTO with an id %d from the database", id));

        return hallMapper.mapHallToHallDto(getHallById(id));
    }

    public HallDto updateHall(HallRequest request, int id) {
        HallDto hallDto = getHallDtoById(id);

        hallRepository.save(new Hall(id, request.getCapacity(), cinemaService.getCinemaById(request.getCinemaId())));

        log.info(String.format("Hall with an id %d has been updated", id));

        return hallDto;
    }

    public HallDto deleteHall(int id) {
        HallDto hallDto = getHallDtoById(id);

        hallRepository.deleteById(id);

        log.info(String.format("Hall with an id %d has been deleted", id));

        return hallDto;
    }
}


