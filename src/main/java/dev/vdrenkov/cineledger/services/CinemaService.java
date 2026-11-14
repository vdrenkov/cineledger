package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.CinemaAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CinemaNotFoundException;
import dev.vdrenkov.cineledger.mappers.CinemaMapper;
import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.requests.CinemaRequest;
import dev.vdrenkov.cineledger.repositories.CinemaRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    public Cinema addCinema(CinemaRequest cinemaRequest) {

        cinemaValidation(cinemaRequest);

        return cinemaRepository.save(new Cinema(cinemaRequest.address(), cinemaRequest.city(), 0));
    }

    public List<CinemaDto> getAllCinemas(String city, String address) {
        if (city == null && address == null) {

            throw new IllegalArgumentException("You haven't provided any query parameters");
        } else if (city != null && address != null) {

            return CinemaMapper.mapCinemaToCinemaDtoList(cinemaRepository.findAllByCityAndAddress(city, address));
        } else if (city != null) {

            return CinemaMapper.mapCinemaToCinemaDtoList(cinemaRepository.findAllByCity(city));
        } else {

            return CinemaMapper.mapCinemaToCinemaDtoList(cinemaRepository.findAllByAddress(address));
        }
    }

    public Cinema getCinemaById(int id) {

        return cinemaRepository.findById(id).orElseThrow(() -> {

            return new CinemaNotFoundException(ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE);
        });
    }

    public CinemaDto getCinemaDtoById(int id) {

        return CinemaMapper.mapCinemaToCinemaDto(getCinemaById(id));
    }

    public CinemaDto updateCinema(CinemaRequest cinemaRequest, int id) {
        CinemaDto cinemaDto = getCinemaDtoById(id);

        cinemaRepository.save(
            new Cinema(id, cinemaRequest.address(), cinemaRequest.city(), cinemaDto.averageRating()));

        return cinemaDto;
    }

    public CinemaDto updateCinemaAverageRating(double newRating, int cinemaId) {
        final Cinema cinema = getCinemaById(cinemaId);
        CinemaDto cinemaDto = CinemaMapper.mapCinemaToCinemaDto(cinema);
        cinema.setAverageRating(newRating);
        cinemaDto = new CinemaDto(cinemaDto.id(), cinemaDto.address(), cinemaDto.city(), newRating);

        cinemaRepository.save(cinema);

        return cinemaDto;
    }

    public CinemaDto deleteCinema(int id) {
        CinemaDto cinemaDto = getCinemaDtoById(id);

        cinemaRepository.deleteById(id);

        return cinemaDto;
    }

    private void cinemaValidation(CinemaRequest cinemaRequest) {
        cinemaRepository.findByCityAndAddress(cinemaRequest.city(), cinemaRequest.address()).ifPresent(cinema -> {

            throw new CinemaAlreadyExistsException(ExceptionMessages.CINEMA_ALREADY_EXISTS_MESSAGE);
        });
    }
}
