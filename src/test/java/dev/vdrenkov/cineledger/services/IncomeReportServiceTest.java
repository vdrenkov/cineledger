package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.entities.Hall;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.testutils.constants.ItemConstants;
import dev.vdrenkov.cineledger.testutils.constants.OrderConstants;
import dev.vdrenkov.cineledger.testutils.constants.ProjectionConstants;
import dev.vdrenkov.cineledger.testutils.constants.ReportConstants;
import dev.vdrenkov.cineledger.testutils.factories.OrderFactory;
import dev.vdrenkov.cineledger.testutils.factories.TicketFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Tests income report service behavior.
 */
@ExtendWith(MockitoExtension.class)
class IncomeReportServiceTest {

    @Mock
    private OrderService orderService;

    @Mock
    private TicketService ticketService;

    @Mock
    private CinemaService cinemaService;

    @Mock
    private HallService hallService;

    @Mock
    private ItemService itemService;

    @Mock
    private MovieService movieService;

    @Mock
    private UserService userService;

    @InjectMocks
    private IncomeReportService incomeReportService;

    /**
     * Verifies that get All Incomes By Cinema Id double Returned success.
     */
    @Test
    void testGetAllIncomesByCinemaId_doubleReturned_success() {
        when(cinemaService.getCinemaById(anyInt())).thenReturn(new Cinema());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        double result = incomeReportService.getAllIncomesByCinemaId(ReportConstants.ID, ReportConstants.START_DATE,
            ReportConstants.END_DATE);

        Assertions.assertEquals(OrderConstants.TOTAL_PRICE, result, 0.0);
    }

    /**
     * Verifies that get All Incomes By Cinema Id cinema Id Different returns Zero.
     */
    @Test
    void testGetAllIncomesByCinemaId_cinemaIdDifferent_returnsZero() {
        final List<Order> orders = OrderFactory.getDefaultOrderList();
        orders.get(0).getTickets().get(0).getProjection().getHall().getCinema().setId(0);

        when(cinemaService.getCinemaById(anyInt())).thenReturn(new Cinema());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(orders);

        double result = incomeReportService.getAllIncomesByCinemaId(ReportConstants.ID, ReportConstants.START_DATE,
            ReportConstants.END_DATE);

        assertEquals(0, result, 0.0);
    }

    /**
     * Verifies that get All Incomes By Hall Id double Returned success.
     */
    @Test
    void testGetAllIncomesByHallId_doubleReturned_success() {
        when(hallService.getHallById(anyInt())).thenReturn(new Hall());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        double result = incomeReportService.getAllIncomesByHallId(ReportConstants.ID, ReportConstants.START_DATE,
            ReportConstants.END_DATE);

        Assertions.assertEquals(OrderConstants.TOTAL_PRICE, result, 0.0);
    }

    /**
     * Verifies that get All Incomes By Hall Id hall Id Different returns Zero.
     */
    @Test
    void testGetAllIncomesByHallId_hallIdDifferent_returnsZero() {
        final List<Order> orders = OrderFactory.getDefaultOrderList();
        orders.get(0).getTickets().get(0).getProjection().getHall().setId(0);

        when(hallService.getHallById(anyInt())).thenReturn(new Hall());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(orders);

        double result = incomeReportService.getAllIncomesByHallId(ReportConstants.ID, ReportConstants.START_DATE,
            ReportConstants.END_DATE);

        assertEquals(0, result, 0.0);
    }

    /**
     * Verifies that get All Incomes By Item Id double Returned success.
     */
    @Test
    void testGetAllIncomesByItemId_doubleReturned_success() {
        when(itemService.getItemDtoById(anyInt())).thenReturn(new ItemDto());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        double result = incomeReportService.getAllIncomesByItemId(ReportConstants.ID, ReportConstants.START_DATE,
            ReportConstants.END_DATE);

        Assertions.assertEquals(ItemConstants.PRICE, result, 0.0);
    }

    /**
     * Verifies that get All Incomes By Movie Id double Returned success.
     */
    @Test
    void testGetAllIncomesByMovieId_doubleReturned_success() {
        when(movieService.getMovieById(anyInt())).thenReturn(new Movie());
        when(ticketService.getTicketsByDateBetween(any(), any())).thenReturn(TicketFactory.getDefaultTicketList());

        double result = incomeReportService.getAllIncomesByMovieId(ReportConstants.ID, ReportConstants.START_DATE,
            ReportConstants.END_DATE);

        Assertions.assertEquals(ProjectionConstants.PRICE, result, 0.0);
    }

    /**
     * Verifies that get All Incomes By User Id double Returned success.
     */
    @Test
    void testGetAllIncomesByUserId_doubleReturned_success() {
        when(userService.getUserById(anyInt())).thenReturn(new User());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        double result = incomeReportService.getAllIncomesByUserId(ReportConstants.ID, ReportConstants.START_DATE,
            ReportConstants.END_DATE);

        Assertions.assertEquals(OrderConstants.TOTAL_PRICE, result, 0.0);
    }
}



