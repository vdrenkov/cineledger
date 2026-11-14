package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeReportService {

    private final CinemaService cinemaService;
    private final HallService hallService;
    private final ItemService itemService;
    private final MovieService movieService;
    private final UserService userService;
    private final OrderService orderService;
    private final TicketService ticketService;

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

    public double getAllIncomesByCinemaId(int id, LocalDate startDate, LocalDate endDate) {
        double incomes = 0;

        cinemaService.getCinemaById(id);

        final List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

        for (Order order : allOrders) {
            if (isOrderWithinDateRange(order, startDate, endDate) && isOrderFromCinema(order, id)) {
                incomes += order.getTotalPrice();
            }
        }

        return incomes;
    }

    public double getAllIncomesByHallId(int id, LocalDate startDate, LocalDate endDate) {
        double incomes = 0;

        hallService.getHallById(id);

        final List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

        for (Order order : allOrders) {
            if (isOrderWithinDateRange(order, startDate, endDate) && isOrderFromHall(order, id)) {
                incomes += order.getTotalPrice();
            }
        }

        return incomes;
    }

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

        return incomes;
    }

    public double getAllIncomesByMovieId(int id, LocalDate startDate, LocalDate endDate) {
        double incomes = 0;

        movieService.getMovieById(id);

        final List<Ticket> allTickets = ticketService.getTicketsByDateBetween(startDate, endDate);

        for (Ticket ticket : allTickets) {
            if (isTicketWithinDateRange(ticket, startDate, endDate) && isTicketForMovie(ticket, id)) {
                incomes += ticket.getProjection().getPrice();
            }
        }

        return incomes;
    }

    public double getAllIncomesByUserId(int id, LocalDate startDate, LocalDate endDate) {
        double incomes = 0;

        userService.getUserById(id);

        final List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

        for (Order order : allOrders) {
            if (isOrderWithinDateRange(order, startDate, endDate) && isOrderFromUser(order, id)) {
                incomes += order.getTotalPrice();
            }
        }

        return incomes;
    }

    private static boolean isOrderFromCinema(Order order, int cinemaId) {
        final List<Ticket> tickets = order.getTickets();

        for (Ticket ticket : tickets) {
            if (cinemaId == ticket.getProjection().getHall().getCinema().getId()) {

                return true;
            }
        }

        return false;
    }

    private static boolean isOrderFromHall(Order order, int hallId) {
        final List<Ticket> tickets = order.getTickets();

        for (Ticket ticket : tickets) {
            if (hallId == ticket.getProjection().getHall().getId()) {

                return true;
            }
        }

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
