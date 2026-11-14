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
class IncomeReportServiceTest {
    private static final LocalDate START = LocalDate.of(2026, 1, 1);
    private static final LocalDate END = LocalDate.of(2026, 12, 31);
    @Mock private CinemaRepository cinemaRepository;
    @Mock private HallRepository hallRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private MovieRepository movieRepository;
    @Mock private UserRepository userRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private TicketRepository ticketRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @InjectMocks private IncomeReportService reportService;

    @Test
    void income_reversedDates_rejectsBeforeDatabaseAccess() {
        assertThrows(
                IllegalArgumentException.class,
                () -> reportService.getAllIncomesByUserId(1, END, START));
        verifyNoInteractions(userRepository, orderRepository);
    }

    @Test
    void cinemaIncome_missingCinema_returnsNotFound() {
        assertThrows(
                CinemaNotFoundException.class,
                () -> reportService.getAllIncomesByCinemaId(1, START, END));
        verifyNoInteractions(ticketRepository);
    }

    @Test
    void hallIncome_missingHall_returnsNotFound() {
        assertThrows(
                HallNotFoundException.class,
                () -> reportService.getAllIncomesByHallId(1, START, END));
        verifyNoInteractions(ticketRepository);
    }

    @Test
    void itemIncome_missingItem_returnsNotFound() {
        assertThrows(
                ItemNotFoundException.class,
                () -> reportService.getAllIncomesByItemId(1, START, END));
        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void movieIncome_missingMovie_returnsNotFound() {
        assertThrows(
                MovieNotFoundException.class,
                () -> reportService.getAllIncomesByMovieId(1, START, END));
        verifyNoInteractions(ticketRepository);
    }

    @Test
    void customerIncome_missingCustomer_returnsNotFound() {
        assertThrows(
                UserNotFoundException.class,
                () -> reportService.getAllIncomesByUserId(1, START, END));
        verifyNoInteractions(orderRepository);
    }
}
