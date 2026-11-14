package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.HallNotFoundException;
import dev.vdrenkov.cineledger.mappers.HallMapper;
import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.models.requests.HallRequest;
import dev.vdrenkov.cineledger.repositories.HallRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallService {

    private final HallRepository hallRepository;
    private final CinemaService cinemaService;

    public HallService(HallRepository hallRepository, CinemaService cinemaService) {
        this.hallRepository = hallRepository;
        this.cinemaService = cinemaService;
    }

    public Hall addHall(HallRequest request) {

        return hallRepository.save(
                new Hall(request.capacity(), cinemaService.getCinemaById(request.cinemaId())));
    }

    public List<HallDto> getHallsByCinemaId(int cinemaId) {
        final Cinema cinema = cinemaService.getCinemaById(cinemaId);

        return HallMapper.mapHallListToHallDtoList(
                hallRepository.findAllByCinemaId(cinema.getId()));
    }

    public Hall getHallForUpdate(final int id) {
        final Hall stored =
                hallRepository
                        .findByIdForUpdate(id)
                        .orElseThrow(() ->
                                new HallNotFoundException(
                                        ExceptionMessages.HALL_NOT_FOUND_MESSAGE));
        return stored;
    }

    public Hall getHallById(int id) {

        return hallRepository
                .findById(id)
                .orElseThrow(() -> {
                    return new HallNotFoundException(ExceptionMessages.HALL_NOT_FOUND_MESSAGE);
                });
    }

    public HallDto getHallDtoById(int id) {

        return HallMapper.mapHallToHallDto(getHallById(id));
    }

    public HallDto updateHall(HallRequest request, int id) {
        final HallDto hallDto = getHallDtoById(id);

        hallRepository.save(
                new Hall(id, request.capacity(), cinemaService.getCinemaById(request.cinemaId())));

        return hallDto;
    }

    public HallDto deleteHall(int id) {
        final HallDto hallDto = getHallDtoById(id);

        hallRepository.deleteById(id);

        return hallDto;
    }
}
