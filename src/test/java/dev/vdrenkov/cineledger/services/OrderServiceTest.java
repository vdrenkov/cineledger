package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DiscountNotValidException;
import dev.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import dev.vdrenkov.cineledger.exceptions.OrderNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.models.requests.OrderRequest;
import dev.vdrenkov.cineledger.models.requests.TicketRequest;
import dev.vdrenkov.cineledger.repositories.OrderRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import dev.vdrenkov.cineledger.testutils.constants.DiscountConstants;
import dev.vdrenkov.cineledger.testutils.constants.OrderConstants;
import dev.vdrenkov.cineledger.testutils.constants.ReportConstants;
import dev.vdrenkov.cineledger.testutils.factories.ItemFactory;
import dev.vdrenkov.cineledger.testutils.factories.OrderFactory;
import dev.vdrenkov.cineledger.testutils.factories.ProjectionFactory;
import dev.vdrenkov.cineledger.testutils.factories.TicketFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests order service behavior.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    final List<TicketRequest> ticketRequests = Collections.singletonList(TicketFactory.getDefaultTicketRequest());
    final OrderRequest orderRequest = OrderFactory.getDefaultOrderRequest();

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private TicketService ticketService;

    @Mock
    private ItemService itemService;

    @Mock
    private EmailService emailService;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private OrderService orderService;

    /**
     * Verifies that add Order no Exceptions success.
     */
    @Test
    void testAddOrder_noExceptions_success() {
        final Order expected = OrderFactory.getDefaultOrder();

        when(ticketService.getTicketById(anyInt())).thenReturn(TicketFactory.getDefaultTicket());
        when(itemService.getItemById(anyInt())).thenReturn(ItemFactory.getDefaultItem());
        when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
        when(orderRepository.save(any())).thenReturn(OrderFactory.getDefaultOrder());
        when(discountService.applyDiscount(anyDouble(), anyString())).thenReturn(OrderConstants.TOTAL_PRICE);

        final Order order = orderService.addOrder(orderRequest);

        assertEquals(expected, order);
        verify(emailService).sendOrderConfirmationEmail(any(), any());
    }

    /**
     * Verifies that add Order discount Code Not Valid throws Discount Not Found Exception.
     */
    @Test
    void testAddOrder_discountCodeNotValid_throwsDiscountNotFoundException() {
        when(ticketService.getTicketById(anyInt())).thenReturn(TicketFactory.getDefaultTicket());
        when(itemService.getItemById(anyInt())).thenReturn(ItemFactory.getDefaultItem());
        when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
        when(discountService.applyDiscount(anyDouble(), anyString())).thenThrow(
            new DiscountNotValidException(ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE));

        orderRequest.setDiscountCode("Code");
        assertThrows(DiscountNotValidException.class, () -> orderService.addOrder(orderRequest));
    }

    /**
     * Verifies that make Reservation With User Id no Exceptions success.
     */
    @Test
    void testMakeReservationWithUserId_noExceptions_success() {
        final Order expected = OrderFactory.getDefaultOrder();
        final Ticket ticket = new Ticket();
        ticket.setId(OrderConstants.ID);
        ticket.setDateOfPurchase(LocalDate.now());
        ticket.setProjection(ProjectionFactory.getDefaultProjection());

        when(ticketService.addTicket(any())).thenReturn(ticket);
        when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(orderRepository.save(any())).thenReturn(expected);
        when(discountService.applyDiscount(anyDouble(), anyString())).thenReturn(OrderConstants.TOTAL_PRICE);

        final Order order = orderService.makeReservationWithUserId(ticketRequests, OrderConstants.ID,
            DiscountConstants.CODE);

        assertEquals(expected, order);
    }

    /**
     * Verifies that make Reservation With User Id discount Code Not Valid throws Discount Not Found Exception.
     */
    @Test
    void testMakeReservationWithUserId_discountCodeNotValid_throwsDiscountNotFoundException() {
        final Ticket ticket = new Ticket();
        ticket.setId(OrderConstants.ID);
        ticket.setDateOfPurchase(LocalDate.now());
        ticket.setProjection(ProjectionFactory.getDefaultProjection());

        final String invalidDiscount = "ABC";

        when(ticketService.addTicket(any())).thenReturn(ticket);
        when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(discountService.applyDiscount(anyDouble(), anyString())).thenThrow(
            new DiscountNotValidException(ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE));

        assertThrows(DiscountNotValidException.class,
            () -> orderService.makeReservationWithUserId(ticketRequests, OrderConstants.ID, invalidDiscount));
    }

    /**
     * Verifies that make Reservation With User Id invalid User Id throws Not Authorized Exception.
     */
    @Test
    void testMakeReservationWithUserId_invalidUserId_throwsNotAuthorizedException() {
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

        assertThrows(NotAuthorizedException.class,
            () -> orderService.makeReservationWithUserId(ticketRequests, OrderConstants.ID, null));
    }

    /**
     * Verifies that get Orders By User Id no Exceptions success.
     */
    @Test
    void testGetOrdersByUserId_noExceptions_success() {
        final User user = UserFactory.getDefaultUser();

        when(userService.getCurrentUser()).thenReturn(user);
        when(orderRepository.findOrderByUserId(anyInt())).thenReturn(OrderFactory.getDefaultOrderList());

        final List<OrderDto> result = orderService.getOrdersByUserId(OrderConstants.ID);

        assertEquals(OrderFactory.getDefaultOrderDtoList(), result);
    }

    /**
     * Verifies that get Orders By User Id invalid User Id throws Not Authorized Exception.
     */
    @Test
    void testGetOrdersByUserId_invalidUserId_throwsNotAuthorizedException() {
        final User user = UserFactory.getDefaultUser();
        when(userService.getCurrentUser()).thenReturn(user);

        final int invalidUserId = 999;
        assertThrows(NotAuthorizedException.class, () -> orderService.getOrdersByUserId(invalidUserId));
    }

    /**
     * Verifies that update Order no Exceptions success.
     */
    @Test
    void testUpdateOrder_noExceptions_success() {
        final OrderDto expected = OrderFactory.getDefaultOrderDto();

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(OrderFactory.getDefaultOrder()));
        when(ticketService.getTicketById(anyInt())).thenReturn(TicketFactory.getDefaultTicket());
        when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
        when(orderRepository.save(any())).thenReturn(OrderFactory.getDefaultOrder());

        final Item newItem = ItemFactory.getDefaultItem();
        newItem.setId(999);
        when(itemService.getItemById(newItem.getId())).thenReturn(newItem);

        orderRequest.setItemsIds(Collections.singletonList(newItem.getId()));
        final OrderDto result = orderService.updateOrder(orderRequest, OrderConstants.ID);

        orderRequest.setItemsIds(new ArrayList<>());
        orderService.updateOrder(orderRequest, OrderConstants.ID);

        assertEquals(expected, result);
    }

    /**
     * Verifies that update Order Order Not Found Exception fail.
     */
    @Test
    void testUpdateOrder_OrderNotFoundException_fail() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrder(orderRequest, OrderConstants.ID));
    }

    /**
     * Verifies that delete Order no Exceptions fail.
     */
    @Test
    void testDeleteOrder_noExceptions_fail() {
        final OrderDto expected = OrderFactory.getDefaultOrderDto();

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(OrderFactory.getDefaultOrder()));

        final OrderDto result = orderService.deleteOrder(OrderConstants.ID);

        assertEquals(expected, result);
    }

    /**
     * Verifies that delete Order Order Not Found Exception fail.
     */
    @Test
    void testDeleteOrder_OrderNotFoundException_fail() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(OrderConstants.ID));
    }

    /**
     * Verifies that get Orders By Date Between.
     */
    @Test
    void testGetOrdersByDateBetween() {
        final List<Order> expected = OrderFactory.getDefaultOrderList();

        when(orderRepository.findOrdersByDateOfPurchaseBetween(any(), any())).thenReturn(expected);

        final List<Order> orders = orderService.getOrdersByDateBetween(ReportConstants.START_DATE,
            ReportConstants.END_DATE);

        assertEquals(expected, orders);
    }

    /**
     * Verifies that order Mutations are Transactional.
     */
    @Test
    void testOrderMutations_areTransactional() throws NoSuchMethodException {
        final Method addOrder = OrderService.class.getMethod("addOrder", OrderRequest.class);
        final Method updateOrder = OrderService.class.getMethod("updateOrder", OrderRequest.class, int.class);
        final Method reserveOrder = OrderService.class.getMethod("makeReservationWithUserId", List.class, int.class,
            String.class);
        final Method deleteOrder = OrderService.class.getMethod("deleteOrder", int.class);

        assertTrue(addOrder.isAnnotationPresent(Transactional.class));
        assertTrue(updateOrder.isAnnotationPresent(Transactional.class));
        assertTrue(reserveOrder.isAnnotationPresent(Transactional.class));
        assertTrue(deleteOrder.isAnnotationPresent(Transactional.class));
    }
}



