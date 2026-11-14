package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.CategoryNotFoundException;
import dev.vdrenkov.cineledger.exceptions.ItemNotFoundException;
import dev.vdrenkov.cineledger.exceptions.MovieNotFoundException;
import dev.vdrenkov.cineledger.repositories.CategoryRepository;
import dev.vdrenkov.cineledger.repositories.ItemRepository;
import dev.vdrenkov.cineledger.repositories.MovieRepository;
import dev.vdrenkov.cineledger.repositories.OrderItemRepository;
import dev.vdrenkov.cineledger.repositories.TicketRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class StatisticsReportService {
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;
    private final ItemRepository itemRepository;
    private final TicketRepository ticketRepository;
    private final OrderItemRepository orderItemRepository;

    public StatisticsReportService(
            final CategoryRepository categoryRepository,
            final MovieRepository movieRepository,
            final ItemRepository itemRepository,
            final TicketRepository ticketRepository,
            final OrderItemRepository orderItemRepository) {
        this.categoryRepository = categoryRepository;
        this.movieRepository = movieRepository;
        this.itemRepository = itemRepository;
        this.ticketRepository = ticketRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public long getPurchasedTicketsCountByMovieCategory(
            final int categoryId, final LocalDate startDate, final LocalDate endDate) {
        requireValidPeriod(startDate, endDate);

        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE);
        }

        return ticketRepository.purchasedCountByCategoryId(categoryId, startDate, endDate);
    }

    public long getPurchasedTicketsCountByMovieTitle(
            final String title, final LocalDate startDate, final LocalDate endDate) {
        requireValidPeriod(startDate, endDate);
        final var movie =
                movieRepository
                        .findByTitle(title)
                        .orElseThrow(() ->
                                new MovieNotFoundException(
                                        ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE));
        return ticketRepository.purchasedCountByMovieId(movie.getId(), startDate, endDate);
    }

    public long getPurchasedItemsCountByItemName(
            final String name, final LocalDate startDate, final LocalDate endDate) {
        requireValidPeriod(startDate, endDate);
        final var item =
                itemRepository
                        .findByName(name)
                        .orElseThrow(() ->
                                new ItemNotFoundException(
                                        ExceptionMessages.ITEM_NOT_FOUND_MESSAGE));
        return orderItemRepository.quantityByItemId(item.getId(), startDate, endDate);
    }

    private static void requireValidPeriod(final LocalDate startDate, final LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must not follow end date");
        }
    }
}
