package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsReportService {

    private final TicketService ticketService;
    private final MovieService movieService;
    private final CategoryService categoryService;

    private final ItemService itemService;
    private final OrderService orderService;

    public StatisticsReportService(TicketService ticketService, MovieService movieService,
        CategoryService categoryService, ItemService itemService, OrderService orderService) {
        this.ticketService = ticketService;
        this.movieService = movieService;
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.orderService = orderService;
    }

    public int getPurchasedTicketsCountByMovieCategory(int categoryId, LocalDate startDate, LocalDate endDate) {
        categoryService.getCategoryById(categoryId);

        final List<Ticket> allTickets = ticketService.getTicketsByDateBetween(startDate, endDate);

        int totalTickets = 0;
        for (Ticket ticket : allTickets) {
            if (isTicketWithinDateRange(ticket, startDate, endDate) && isTicketForMovieCategory(ticket, categoryId)) {
                totalTickets += 1;
            }
        }

        return totalTickets;
    }

    public int getPurchasedTicketsCountByMovieTitle(String title, LocalDate startDate, LocalDate endDate) {
        movieService.getMovieByTitle(title);
        final List<Integer> movieIds = movieService.getIdsOfMoviesByTitle(title);
        final List<Ticket> tickets = ticketService.getTicketsByDateBetween(startDate, endDate);

        int totalTickets = 0;
        for (Ticket ticket : tickets) {
            if (isTicketWithinDateRange(ticket, startDate, endDate) && isTicketForMovieWithTitle(ticket, movieIds)) {
                totalTickets += 1;
            }
        }

        return totalTickets;
    }

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

        return totalTickets;
    }

    private static boolean isOrderWithinDateRange(Order order, LocalDate startDate, LocalDate endDate) {
        final LocalDate orderDate = order.getDateOfPurchase();
        return orderDate.isEqual(startDate) || (orderDate.isAfter(startDate) && orderDate.isBefore(endDate));
    }

    private static boolean isTicketWithinDateRange(Ticket ticket, LocalDate startDate, LocalDate endDate) {
        final LocalDate ticketDate = ticket.getDateOfPurchase();

        return ticketDate.isEqual(startDate) || ticketDate.isEqual(endDate) || (ticketDate.isAfter(startDate)
            && ticketDate.isBefore(endDate));
    }

    private static boolean isTicketForMovieWithTitle(Ticket ticket, List<Integer> movieIds) {
        final int movieId = ticket.getProjection().getMovie().getId();

        return movieIds.contains(movieId);
    }

    private static boolean isTicketForMovieCategory(Ticket ticket, int id) {
        return id == ticket.getProjection().getMovie().getCategory().getId();
    }
}
