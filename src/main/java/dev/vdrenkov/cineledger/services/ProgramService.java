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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Contains business logic for program operations.
 */
@Service
public class ProgramService {
    private static final Logger log = LoggerFactory.getLogger(ProgramService.class);

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;
    private final CinemaService cinemaService;

    /**
     * Creates a new program service with its required collaborators.
     *
     * @param programRepository
     *     program repository used by the operation
     * @param programMapper
     *     program mapper used by the operation
     * @param cinemaService
     *     cinema service used by the operation
     */
    @Autowired
    public ProgramService(ProgramRepository programRepository, ProgramMapper programMapper,
        CinemaService cinemaService) {
        this.programRepository = programRepository;
        this.programMapper = programMapper;
        this.cinemaService = cinemaService;
    }

    /**
     * Creates and persists program.
     *
     * @param request
     *     request payload containing the submitted data
     * @return requested program value
     */
    public Program addProgram(ProgramRequest request) {
        if (isDateNotValid(request.getProgramDate())) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.DATE_NOT_VALID_MESSAGE));

            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        programRepository
            .findByProgramDateAndCinemaId(request.getProgramDate(), request.getCinemaId())
            .ifPresent(existingProgram -> {

                log.error(String.format("Exception caught: %s", ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE));
                throw new ProgramAlreadyExistsException(ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE);
            });

        log.info("An attempt to add a new program in the database");

        return programRepository.save(
            new Program(request.getProgramDate(), cinemaService.getCinemaById(request.getCinemaId())));
    }

    /**
     * Returns all programs matching the supplied criteria.
     *
     * @param date
     *     date used by the operation
     * @return matching program dto values
     */
    public List<ProgramDto> getAllPrograms(LocalDate date) {
        if (Objects.nonNull(date)) {
            log.info(String.format("All programs with date %s were requested from the database", date));

            return programMapper.mapProgramListToProgramDtoList(programRepository.findAllByProgramDate(date));
        } else {
            log.info("All programs were requested from the database");

            return programMapper.mapProgramListToProgramDtoList(programRepository.findAll());
        }
    }

    /**
     * Returns programs matching the supplied criteria.
     *
     * @param cinemaId
     *     identifier of the target cinema
     * @return matching program dto values
     */
    public List<ProgramDto> getProgramsByCinemaId(int cinemaId) {
        log.info(String.format("All programs with cinema id %d were requested from the database", cinemaId));

        final Cinema cinema = cinemaService.getCinemaById(cinemaId);

        return programMapper.mapProgramListToProgramDtoList(programRepository.findAllByCinemaId(cinema.getId()));
    }

    /**
     * Returns program matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested program value
     */
    public Program getProgramById(int id) {
        log.info(String.format("An attempt to extract a program with an id %d from the database", id));

        return programRepository.findById(id).orElseThrow(() -> {

            log.error(String.format("Exception caught: %s", ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE));

            throw new ProgramNotFoundException(ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Returns program matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return program dto result
     */
    public ProgramDto getProgramDtoById(int id) {
        log.info(String.format("An attempt to extract a program DTO with an id %d from the database", id));

        return programMapper.mapProgramToProgramDto(getProgramById(id));
    }

    /**
     * Updates program and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param programId
     *     identifier of the target program
     * @return program dto result
     */
    public ProgramDto updateProgram(ProgramRequest request, int programId) {
        final ProgramDto programDto = getProgramDtoById(programId);

        if (isDateNotValid(request.getProgramDate())) {

            log.error(String.format("Exception caught: %s", ExceptionMessages.DATE_NOT_VALID_MESSAGE));

            throw new DateNotValidException(ExceptionMessages.DATE_NOT_VALID_MESSAGE);
        }

        programRepository.save(
            new Program(programId, request.getProgramDate(), cinemaService.getCinemaById(request.getCinemaId())));

        log.info(String.format("Program with an id %d has been updated", programId));

        return programDto;
    }

    /**
     * Deletes program and returns the removed state when needed.
     *
     * @param programId
     *     identifier of the target program
     * @return program dto result
     */
    public ProgramDto deleteProgram(int programId) {
        final ProgramDto programDto = getProgramDtoById(programId);

        programRepository.deleteById(programId);

        log.info(String.format("Program with an id %d has been deleted", programId));

        return programDto;
    }

    /**
     * Checks whether the supplied release date violates the application rule set.
     *
     * @param date
     *     date used by the operation
     * @return true when the requested condition holds; otherwise false
     */
    public boolean isDateNotValid(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }
}

