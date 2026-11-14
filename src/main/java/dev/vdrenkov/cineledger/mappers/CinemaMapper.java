package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;

import java.util.List;

public final class CinemaMapper {

    private CinemaMapper() {}

    public static CinemaDto mapCinemaToCinemaDto(Cinema cinema) {

        return new CinemaDto(
                cinema.getId(), cinema.getAddress(), cinema.getCity(), cinema.getAverageRating());
    }

    public static List<CinemaDto> mapCinemaToCinemaDtoList(List<Cinema> cinemaList) {
        return cinemaList.stream().map(CinemaMapper::mapCinemaToCinemaDto).toList();
    }
}
