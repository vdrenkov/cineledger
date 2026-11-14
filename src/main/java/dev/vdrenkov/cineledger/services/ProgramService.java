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
    private final dev.vdrenkov.cineledger.repositories.ProjectionRepository projectionRepository;

    private final ProgramRepository programRepository;
    private final CinemaService cinemaService;

    public ProgramService(
            dev.vdrenkov.cineledger.repositories.ProjectionRepository projectionRepository,
            ProgramRepository programRepository,
            CinemaService cinemaService) {
        this.projectionRepository = projectionRepository;
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
                    throw new ProgramAlreadyExistsException(
                            ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE);
                });

        return programRepository.save(
                new Program(
                        request.programDate(), cinemaService.getCinemaById(request.cinemaId())));
    }

    public List<ProgramDto> getAllPrograms(LocalDate date) {
        if (date != null) {

            return ProgramMapper.mapProgramListToProgramDtoList(
                    programRepository.findAllByProgramDate(date));
        } else {

            return ProgramMapper.mapProgramListToProgramDtoList(programRepository.findAll());
        }
    }

    public List<ProgramDto> getProgramsByCinemaId(int cinemaId) {

        final Cinema cinema = cinemaService.getCinemaById(cinemaId);

        return ProgramMapper.mapProgramListToProgramDtoList(
                programRepository.findAllByCinemaId(cinema.getId()));
    }

    public Program getProgramById(int id) {

        return programRepository
                .findById(id)
                .orElseThrow(() -> {
                    return new ProgramNotFoundException(
                            ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE);
                });
    }

    public ProgramDto getProgramDtoById(int id) {

        return ProgramMapper.mapProgramToProgramDto(getProgramById(id));
    }

    @org.springframework.transaction.annotation.Transactional
    public ProgramDto updateProgram(final ProgramRequest request, final int programId) {
        final Program stored = getProgramForUpdate(programId);
        final ProgramDto previous = ProgramMapper.mapProgramToProgramDto(stored);

        if (isDateNotValid(request.programDate())) {
            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        if ((!stored.getProgramDate().equals(request.programDate())
                        || stored.getCinema().getId() != request.cinemaId())
                && projectionRepository.existsByProgramId(programId)) {
            throw new dev.vdrenkov.cineledger.exceptions.ResourceInUseException(
                    "A program with screenings cannot change its date or cinema");
        }

        stored.setProgramDate(request.programDate());
        stored.setCinema(cinemaService.getCinemaById(request.cinemaId()));
        programRepository.save(stored);
        return previous;
    }

    public ProgramDto deleteProgram(int programId) {
        final ProgramDto programDto = getProgramDtoById(programId);

        programRepository.deleteById(programId);

        return programDto;
    }

    public static boolean isDateNotValid(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    public Program getProgramForUpdate(final int id) {
        return programRepository
                .findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ProgramNotFoundException(ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE));
    }
}
