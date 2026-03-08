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
 * Calculates income-focused reporting data for the cinema domain.
 */
@Service
public class IncomeReportService {
    private static final Logger log = LoggerFactory.getLogger(IncomeReportService.class);

    private final CinemaService cinemaService;
    private final HallService hallService;
    private final ItemService itemService;
    private final MovieService movieService;
    private final UserService userService;
    private final OrderService orderService;
    private final TicketService ticketService;

    /**
     * Creates a new income report service with its required collaborators.
     *
     * @param cinemaService
     *     cinema service used by the operation
     * @param hallService
     *     hall service used by the operation
     * @param itemService
     *     item service used by the operation
     * @param movieService
     *     movie service used by the operation
     * @param userService
     *     user service used by the operation
     * @param orderService
     *     order service used by the operation
     * @param ticketService
     *     ticket service used by the operation
     */
    @Autowired
    public IncomeReportService(CinemaService cinemaService, HallService hallService, ItemService itemService,
        MovieService movieService, UserService userService, OrderService orderService, TicketService ticketService) {
        this.cinemaService = cinemaService;
        this.hallService = hallService;
        this.itemService = itemService;
        this.movieService = movieService;
        this.userService = userService;
        this.orderService = orderService;
        this.ticketService = ticketService;
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return requested double value
     */
    public double getAllIncomesByCinemaId(int id, LocalDate startDate, LocalDate endDate) {
        double incomes = 0;

        cinemaService.getCinemaById(id);

        final List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

        for (Order order : allOrders) {
            if (isOrderWithinDateRange(order, startDate, endDate) && isOrderFromCinema(order, id)) {
                incomes += order.getTotalPrice();
            }
        }

        log.info("All incomes by cinema id {} calculated", id);

        return incomes;
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return requested double value
     */
    public double getAllIncomesByHallId(int id, LocalDate startDate, LocalDate endDate) {
        double incomes = 0;

        hallService.getHallById(id);

        final List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

        for (Order order : allOrders) {
            if (isOrderWithinDateRange(order, startDate, endDate) && isOrderFromHall(order, id)) {
                incomes += order.getTotalPrice();
            }
        }

        log.info("All incomes by hall id {} calculated", id);

        return incomes;
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return requested double value
     */
    public double getAllIncomesByItemId(int id, LocalDate startDate, LocalDate endDate) {
        double incomes = 0;

        itemService.getItemDtoById(id);

        final List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

        for (Order order : allOrders) {
            if (isOrderWithinDateRange(order, startDate, endDate)) {
                for (Item item : order.getItems()) {
                    if (id == item.getId()) {
                        incomes += item.getPrice();
                    }
                }
            }
        }

        log.info("All incomes by item id {} calculated", id);

        return incomes;
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return requested double value
     */
    public double getAllIncomesByMovieId(int id, LocalDate startDate, LocalDate endDate) {
        double incomes = 0;

        movieService.getMovieById(id);

        final List<Ticket> allTickets = ticketService.getTicketsByDateBetween(startDate, endDate);

        for (Ticket ticket : allTickets) {
            if (isTicketWithinDateRange(ticket, startDate, endDate) && isTicketForMovie(ticket, id)) {
                incomes += ticket.getProjection().getPrice();
            }
        }

        log.info("All incomes by movie id {} calculated", id);

        return incomes;
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return requested double value
     */
    public double getAllIncomesByUserId(int id, LocalDate startDate, LocalDate endDate) {
        double incomes = 0;

        userService.getUserById(id);

        final List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

        for (Order order : allOrders) {
            if (isOrderWithinDateRange(order, startDate, endDate) && isOrderFromUser(order, id)) {
                incomes += order.getTotalPrice();
            }
        }

        log.info("All incomes by user id {} calculated", id);

        return incomes;
    }

    private static boolean isOrderFromCinema(Order order, int cinemaId) {
        final List<Ticket> tickets = order.getTickets();

        for (Ticket ticket : tickets) {
            if (cinemaId == ticket.getProjection().getHall().getCinema().getId()) {

                log.info("Order is from cinema");

                return true;
            }
        }

        log.info("Order is not from cinema");

        return false;
    }

    private static boolean isOrderFromHall(Order order, int hallId) {
        final List<Ticket> tickets = order.getTickets();

        for (Ticket ticket : tickets) {
            if (hallId == ticket.getProjection().getHall().getId()) {

                log.info("Order is from hall");

                return true;
            }
        }

        log.info("Order is not from hall");

        return false;
    }

    private static boolean isTicketForMovie(Ticket ticket, int movieId) {
        return movieId == ticket.getProjection().getMovie().getId();
    }

    private static boolean isOrderFromUser(Order order, int userId) {
        return userId == order.getUser().getId();
    }

    private static boolean isTicketWithinDateRange(Ticket ticket, LocalDate startDate, LocalDate endDate) {
        final LocalDate ticketDate = ticket.getDateOfPurchase();

        return ticketDate.isEqual(startDate) || ticketDate.isEqual(endDate) || (ticketDate.isAfter(startDate)
            && ticketDate.isBefore(endDate));
    }

    private static boolean isOrderWithinDateRange(Order order, LocalDate startDate, LocalDate endDate) {
        final LocalDate orderDate = order.getDateOfPurchase();

        return orderDate.isEqual(startDate) || (orderDate.isAfter(startDate) && orderDate.isBefore(endDate));
    }
}


