package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.HallDto;
import dev.vdrenkov.cineledger.models.entities.Hall;

import java.util.List;

public final class HallMapper {

    private HallMapper() {}

    public static HallDto mapHallToHallDto(Hall hall) {

        return new HallDto(
                hall.getId(),
                hall.getCapacity(),
                CinemaMapper.mapCinemaToCinemaDto(hall.getCinema()));
    }

    public static List<HallDto> mapHallListToHallDtoList(List<Hall> halls) {
        return halls.stream().map(HallMapper::mapHallToHallDto).toList();
    }
}
