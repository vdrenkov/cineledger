package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.*;
import dev.vdrenkov.cineledger.repositories.*;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class IncomeReportService {
    private final CinemaRepository cinemaRepository;
    private final HallRepository hallRepository;
    private final ItemRepository itemRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final OrderItemRepository orderItemRepository;

    public IncomeReportService(
            final CinemaRepository cinemaRepository,
            final HallRepository hallRepository,
            final ItemRepository itemRepository,
            final MovieRepository movieRepository,
            final UserRepository userRepository,
            final OrderRepository orderRepository,
            final TicketRepository ticketRepository,
            final OrderItemRepository orderItemRepository) {
        this.cinemaRepository = cinemaRepository;
        this.hallRepository = hallRepository;
        this.itemRepository = itemRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public BigDecimal getAllIncomesByCinemaId(
            final int id, final LocalDate startDate, final LocalDate endDate) {
        requireValidPeriod(startDate, endDate);

        if (!cinemaRepository.existsById(id)) {
            throw new CinemaNotFoundException(ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE);
        }

        return ticketRepository.incomeByCinemaId(id, startDate, endDate);
    }

    public BigDecimal getAllIncomesByHallId(
            final int id, final LocalDate startDate, final LocalDate endDate) {
        requireValidPeriod(startDate, endDate);

        if (!hallRepository.existsById(id)) {
            throw new HallNotFoundException(ExceptionMessages.HALL_NOT_FOUND_MESSAGE);
        }

        return ticketRepository.incomeByHallId(id, startDate, endDate);
    }

    public BigDecimal getAllIncomesByItemId(
            final int id, final LocalDate startDate, final LocalDate endDate) {
        requireValidPeriod(startDate, endDate);

        if (!itemRepository.existsById(id)) {
            throw new ItemNotFoundException(ExceptionMessages.ITEM_NOT_FOUND_MESSAGE);
        }

        return orderItemRepository.incomeByItemId(id, startDate, endDate);
    }

    public BigDecimal getAllIncomesByMovieId(
            final int id, final LocalDate startDate, final LocalDate endDate) {
        requireValidPeriod(startDate, endDate);

        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException(ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE);
        }

        return ticketRepository.incomeByMovieId(id, startDate, endDate);
    }

    public BigDecimal getAllIncomesByUserId(
            final int id, final LocalDate startDate, final LocalDate endDate) {
        requireValidPeriod(startDate, endDate);

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND_MESSAGE);
        }

        return orderRepository.incomeByUserId(id, startDate, endDate);
    }

    private static void requireValidPeriod(final LocalDate startDate, final LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must not follow end date");
        }
    }
}
