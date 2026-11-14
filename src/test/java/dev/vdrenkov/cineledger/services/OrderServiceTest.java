package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dev.vdrenkov.cineledger.exceptions.*;
import dev.vdrenkov.cineledger.models.entities.*;
import dev.vdrenkov.cineledger.models.requests.OrderRequest;
import dev.vdrenkov.cineledger.repositories.OrderItemRepository;
import dev.vdrenkov.cineledger.repositories.OrderRepository;
import dev.vdrenkov.cineledger.testutils.factories.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private UserService userService;
    @Mock private TicketService ticketService;
    @Mock private ItemService itemService;
    @Mock private EmailService emailService;
    @Mock private DiscountService discountService;
    @Captor private ArgumentCaptor<Iterable<OrderItem>> savedLines;
    @InjectMocks private OrderService orderService;
    private User user;
    private Item item;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        user = UserFactory.getDefaultUser();
        item = ItemFactory.getDefaultItem();
        ticket = TicketFactory.getDefaultTicket();
    }

    @Test
    void addOrder_decimalPrices_preservesExactTotal() {
        setPrices("0.10", "0.20");
        stubResources();
        stubSave();

        final Order order = orderService.addOrder(request(null));

        assertEquals(new BigDecimal("0.30"), order.getTotalPrice());
        assertEquals(new BigDecimal("0.10"), ticket.getPaidPrice());
        verify(orderItemRepository).saveAll(savedLines.capture());
        final OrderItem line = savedLines.getValue().iterator().next();
        assertEquals(new BigDecimal("0.20"), line.getPaidTotal());
        assertEquals(54, item.getQuantity());
    }

    @Test
    void addOrder_discountRounding_allocatesTheExactDiscountedTotal() {
        setPrices("10.25", "2.75");
        stubResources();
        stubSave();
        when(discountService.getDiscountPercentage("1234")).thenReturn(10);

        final Order order = orderService.addOrder(request("1234"));

        assertEquals(new BigDecimal("11.70"), order.getTotalPrice());
        assertEquals(new BigDecimal("9.23"), ticket.getPaidPrice());
        verify(orderItemRepository).saveAll(savedLines.capture());
        assertEquals(
                new BigDecimal("2.47"), savedLines.getValue().iterator().next().getPaidTotal());
    }

    @Test
    void addOrder_duplicateTicketIds_rejectsBeforeDatabaseAccess() {
        final var request =
                new OrderRequest(
                        user.getId(), List.of(ticket.getId(), ticket.getId()), List.of(), null);

        assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(request));

        verifyNoInteractions(orderRepository, ticketService, itemService);
    }

    @Test
    void addOrder_unavailableStock_doesNotSaveOrNotify() {
        item.setQuantity(0);
        stubResources();

        assertThrows(NoAvailableItemsException.class, () -> orderService.addOrder(request(null)));

        verifyNoInteractions(orderRepository, emailService);
    }

    @Test
    void addOrder_invalidDiscount_doesNotSaveOrNotify() {
        stubResources();
        when(discountService.getDiscountPercentage("invalid"))
                .thenThrow(new DiscountNotValidException("Invalid code"));

        assertThrows(
                DiscountNotValidException.class, () -> orderService.addOrder(request("invalid")));

        verifyNoInteractions(orderRepository, emailService);
    }

    @Test
    void getOrderById_otherCustomer_isForbidden() {
        final Order order = OrderFactory.getDefaultOrder();
        final User another = UserFactory.getDefaultUser();
        another.setId(99);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(userService.getCurrentUser()).thenReturn(another);

        assertThrows(NotAuthorizedException.class, () -> orderService.getOrderById(order.getId()));

        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void updateOrder_existingConcession_preservesAgreedUnitPrice() {
        final Order order = OrderFactory.getDefaultOrder();
        final OrderItem previous =
                new OrderItem(
                        order.getId(),
                        item.getId(),
                        item.getName(),
                        1,
                        new BigDecimal("13.00"),
                        new BigDecimal("13.00"));
        item.setPrice(new BigDecimal("99.00"));
        item.setQuantity(10);
        stubResources();
        stubSave();
        when(orderRepository.findByIdForUpdate(order.getId())).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of(previous));

        final var result = orderService.updateOrder(request(null), order.getId());

        assertEquals(new BigDecimal("13.00"), result.items().getFirst().unitPrice());
        assertEquals(10, item.getQuantity());
        verify(orderItemRepository).saveAll(savedLines.capture());
        assertEquals(
                new BigDecimal("13.00"), savedLines.getValue().iterator().next().getUnitPrice());
    }

    @Test
    void updateOrder_missingOrder_returnsNotFound() {
        when(orderRepository.findByIdForUpdate(99)).thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class, () -> orderService.updateOrder(request(null), 99));
    }

    @Test
    void deleteOrder_existingOrder_restoresQuantitiesAndCancelsTickets() {
        final Order order = OrderFactory.getDefaultOrder();
        final OrderItem previous =
                new OrderItem(
                        order.getId(),
                        item.getId(),
                        item.getName(),
                        2,
                        item.getPrice(),
                        item.getPrice().multiply(BigDecimal.TWO));
        item.setQuantity(0);
        when(orderRepository.findByIdForUpdate(order.getId())).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of(previous));
        when(itemService.getItemForUpdate(item.getId())).thenReturn(item);

        orderService.deleteOrder(order.getId());

        assertEquals(2, item.getQuantity());
        verify(orderRepository).delete(order);
        verify(ticketService).cancelTickets(order.getTickets());
    }

    @Test
    void deleteOrder_missingOrder_returnsNotFound() {
        when(orderRepository.findByIdForUpdate(99)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(99));
    }

    @Test
    void makeReservation_otherCustomer_isForbidden() {
        when(userService.isCurrentUserAuthorized(99)).thenReturn(false);

        assertThrows(
                NotAuthorizedException.class,
                () ->
                        orderService.makeReservationWithUserId(
                                List.of(TicketFactory.getDefaultTicketRequest()), 99, null));

        verifyNoInteractions(ticketService, orderRepository);
    }

    @Test
    void makeReservation_emptyTicketList_isRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.makeReservationWithUserId(List.of(), 99, null));
        verifyNoInteractions(ticketService, orderRepository);
    }

    private OrderRequest request(final String discountCode) {
        return new OrderRequest(
                user.getId(), List.of(ticket.getId()), List.of(item.getId()), discountCode);
    }

    private void setPrices(final String ticketPrice, final String itemPrice) {
        final Projection projection = ProjectionFactory.getDefaultProjection();
        projection.setPrice(new BigDecimal(ticketPrice));
        ticket.setProjection(projection);
        item.setPrice(new BigDecimal(itemPrice));
    }

    private void stubResources() {
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(ticketService.getTicketForUpdate(ticket.getId())).thenReturn(ticket);
        when(itemService.getItemForUpdate(item.getId())).thenReturn(item);
    }

    private void stubSave() {
        when(orderRepository.saveAndFlush(any()))
                .thenAnswer(invocation -> {
                    final Order order = invocation.getArgument(0);
                    order.setId(1);
                    return order;
                });
    }
}
