package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.vdrenkov.cineledger.exceptions.*;
import dev.vdrenkov.cineledger.repositories.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class StatisticsReportServiceTest {
    private static final LocalDate START = LocalDate.of(2026, 1, 1);
    private static final LocalDate END = LocalDate.of(2026, 12, 31);
    @Mock private CategoryRepository categoryRepository;
    @Mock private MovieRepository movieRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private TicketRepository ticketRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @InjectMocks private StatisticsReportService reportService;

    @Test
    void ticketCount_reversedDates_rejectsBeforeDatabaseAccess() {
        assertThrows(
                IllegalArgumentException.class,
                () -> reportService.getPurchasedTicketsCountByMovieCategory(1, END, START));
        verifyNoInteractions(categoryRepository, ticketRepository);
    }

    @Test
    void ticketCount_missingCategory_returnsNotFound() {
        assertThrows(
                CategoryNotFoundException.class,
                () -> reportService.getPurchasedTicketsCountByMovieCategory(1, START, END));
    }

    @Test
    void ticketCount_missingMovie_returnsNotFound() {
        assertThrows(
                MovieNotFoundException.class,
                () -> reportService.getPurchasedTicketsCountByMovieTitle("Missing", START, END));
    }

    @Test
    void itemCount_missingItem_returnsNotFound() {
        assertThrows(
                ItemNotFoundException.class,
                () -> reportService.getPurchasedItemsCountByItemName("Missing", START, END));
    }
}
