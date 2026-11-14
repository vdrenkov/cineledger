package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DateNotValidException;
import dev.vdrenkov.cineledger.exceptions.ProgramAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.ProgramNotFoundException;
import dev.vdrenkov.cineledger.mappers.ProgramMapper;
import dev.vdrenkov.cineledger.models.dtos.ProgramDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.entities.Program;
import dev.vdrenkov.cineledger.models.requests.ProgramRequest;
import dev.vdrenkov.cineledger.repositories.ProgramRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final CinemaService cinemaService;

    public ProgramService(ProgramRepository programRepository, CinemaService cinemaService) {
        this.programRepository = programRepository;
        this.cinemaService = cinemaService;
    }

    public Program addProgram(ProgramRequest request) {
        if (isDateNotValid(request.programDate())) {

            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        programRepository
            .findByProgramDateAndCinemaId(request.programDate(), request.cinemaId())
            .ifPresent(existingProgram -> {

                throw new ProgramAlreadyExistsException(ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE);
            });

        return programRepository.save(
            new Program(request.programDate(), cinemaService.getCinemaById(request.cinemaId())));
    }

    public List<ProgramDto> getAllPrograms(LocalDate date) {
        if (date != null) {

            return ProgramMapper.mapProgramListToProgramDtoList(programRepository.findAllByProgramDate(date));
        } else {

            return ProgramMapper.mapProgramListToProgramDtoList(programRepository.findAll());
        }
    }

    public List<ProgramDto> getProgramsByCinemaId(int cinemaId) {

        final Cinema cinema = cinemaService.getCinemaById(cinemaId);

        return ProgramMapper.mapProgramListToProgramDtoList(programRepository.findAllByCinemaId(cinema.getId()));
    }

    public Program getProgramById(int id) {

        return programRepository.findById(id).orElseThrow(() -> {

            return new ProgramNotFoundException(ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE);
        });
    }

    public ProgramDto getProgramDtoById(int id) {

        return ProgramMapper.mapProgramToProgramDto(getProgramById(id));
    }

    public ProgramDto updateProgram(ProgramRequest request, int programId) {
        final ProgramDto programDto = getProgramDtoById(programId);

        if (isDateNotValid(request.programDate())) {

            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        programRepository.save(
            new Program(programId, request.programDate(), cinemaService.getCinemaById(request.cinemaId())));

        return programDto;
    }

    public ProgramDto deleteProgram(int programId) {
        final ProgramDto programDto = getProgramDtoById(programId);

        programRepository.deleteById(programId);

        return programDto;
    }

    public static boolean isDateNotValid(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }
}
