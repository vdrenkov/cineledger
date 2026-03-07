package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Calculates aggregate statistics used by the reporting endpoints.
 */
@Service
public class StatisticsReportService {
    private static final Logger log = LoggerFactory.getLogger(StatisticsReportService.class);

    private final TicketService ticketService;
    private final MovieService movieService;
    private final CategoryService categoryService;

    private final ItemService itemService;
    private final OrderService orderService;

    /**
     * Creates a new statistics report service with its required collaborators.
     *
     * @param ticketService
     *     ticket service used by the operation
     * @param movieService
     *     movie service used by the operation
     * @param categoryService
     *     category service used by the operation
     * @param itemService
     *     item service used by the operation
     * @param orderService
     *     order service used by the operation
     */
    @Autowired
    public StatisticsReportService(TicketService ticketService, MovieService movieService,
        CategoryService categoryService, ItemService itemService, OrderService orderService) {
        this.ticketService = ticketService;
        this.movieService = movieService;
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.orderService = orderService;
    }

    /**
     * Returns purchased tickets count matching the supplied criteria.
     *
     * @param categoryId
     *     identifier of the target category
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return requested int value
     */
    public int getPurchasedTicketsCountByMovieCategory(int categoryId, LocalDate startDate, LocalDate endDate) {
        categoryService.getCategoryById(categoryId);

        final List<Ticket> allTickets = ticketService.getTicketsByDateBetween(startDate, endDate);

        log.info("Retrieving all incomes by category ID");

        int totalTickets = 0;
        for (Ticket ticket : allTickets) {
            if (isTicketWithinDateRange(ticket, startDate, endDate) && isTicketForMovieCategory(ticket, categoryId)) {
                totalTickets += 1;
            }
        }

        return totalTickets;
    }

    /**
     * Returns purchased tickets count matching the supplied criteria.
     *
     * @param title
     *     title text to search for
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return requested int value
     */
    public int getPurchasedTicketsCountByMovieTitle(String title, LocalDate startDate, LocalDate endDate) {
        movieService.getMovieByTitle(title);
        final List<Integer> movieIds = movieService.getIdsOfMoviesByTitle(title);
        final List<Ticket> tickets = ticketService.getTicketsByDateBetween(startDate, endDate);

        log.info(String.format("Retrieving purchased tickets count for movie with title '%s' between %s and %s", title,
            startDate, endDate));

        int totalTickets = 0;
        for (Ticket ticket : tickets) {
            if (isTicketWithinDateRange(ticket, startDate, endDate) && isTicketForMovieWithTitle(ticket, movieIds)) {
                totalTickets += 1;
            }
        }

        return totalTickets;
    }

    /**
     * Returns purchased items count matching the supplied criteria.
     *
     * @param name
     *     name used by the operation
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return requested int value
     */
    public int getPurchasedItemsCountByItemName(String name, LocalDate startDate, LocalDate endDate) {
        int totalTickets = 0;

        itemService.getItemDtoByName(name);
        final List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

        for (Order order : allOrders) {
            if (isOrderWithinDateRange(order, startDate, endDate)) {
                for (Item item : order.getItems()) {
                    if (name.equals(item.getName())) {
                        totalTickets += 1;
                    }
                }
            }
        }

        log.info(
            String.format("Retrieving purchased items count for item with name '%s' between %s and %s", name, startDate,
                endDate));

        return totalTickets;
    }

    private boolean isOrderWithinDateRange(Order order, LocalDate startDate, LocalDate endDate) {
        final LocalDate orderDate = order.getDateOfPurchase();
        return orderDate.isEqual(startDate) || (orderDate.isAfter(startDate) && orderDate.isBefore(endDate));
    }

    private boolean isTicketWithinDateRange(Ticket ticket, LocalDate startDate, LocalDate endDate) {
        final LocalDate ticketDate = ticket.getDateOfPurchase();

        return ticketDate.isEqual(startDate) || ticketDate.isEqual(endDate) || (ticketDate.isAfter(startDate)
            && ticketDate.isBefore(endDate));
    }

    private boolean isTicketForMovieWithTitle(Ticket ticket, List<Integer> movieIds) {
        final int movieId = ticket.getProjection().getMovie().getId();

        return movieIds.contains(movieId);
    }

    private boolean isTicketForMovieCategory(Ticket ticket, int id) {
        return id == ticket.getProjection().getMovie().getCategory().getId();
    }
}


