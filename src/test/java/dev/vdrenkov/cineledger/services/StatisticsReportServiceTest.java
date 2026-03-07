package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import dev.vdrenkov.cineledger.testUtils.constants.ItemConstants;
import dev.vdrenkov.cineledger.testUtils.constants.MovieConstants;
import dev.vdrenkov.cineledger.testUtils.constants.ReportConstants;
import dev.vdrenkov.cineledger.testUtils.factories.OrderFactory;
import dev.vdrenkov.cineledger.testUtils.factories.TicketFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsReportServiceTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private TicketService ticketService;

    @Mock
    private MovieService movieService;

    @Mock
    private ItemService itemService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private StatisticsReportService statisticsReportService;

    @Test
    public void testGetPurchasedTicketsCountByMovieCategory_success() {
        when(categoryService.getCategoryById(anyInt())).thenReturn(new Category());
        when(ticketService.getTicketsByDateBetween(any(), any())).thenReturn(TicketFactory.getDefaultTicketList());

        int result = statisticsReportService.getPurchasedTicketsCountByMovieCategory(MovieConstants.ID,
            ReportConstants.START_DATE, ReportConstants.END_DATE);

        assertEquals(1, result);
    }

    @Test
    public void testGetPurchasedTicketsCountByMovieTitle_success() {
        List<Integer> movieIds = Arrays.asList(1, 2, 3);
        when(movieService.getIdsOfMoviesByTitle(anyString())).thenReturn(movieIds);
        when(ticketService.getTicketsByDateBetween(any(), any())).thenReturn(TicketFactory.getDefaultTicketList());

        int result = statisticsReportService.getPurchasedTicketsCountByMovieTitle(MovieConstants.TITLE,
            ReportConstants.START_DATE, ReportConstants.END_DATE);

        assertEquals(1, result);
    }

    @Test
    public void testGetPurchasedItemsCountByItemName_success() {
        when(itemService.getItemDtoByName(anyString())).thenReturn(new ItemDto());
        when(orderService.getOrdersByDateBetween(any(), any())).thenReturn(OrderFactory.getDefaultOrderList());

        int result = statisticsReportService.getPurchasedItemsCountByItemName(ItemConstants.NAME,
            ReportConstants.START_DATE, ReportConstants.END_DATE);

        Assertions.assertEquals(1, result);
    }
}



