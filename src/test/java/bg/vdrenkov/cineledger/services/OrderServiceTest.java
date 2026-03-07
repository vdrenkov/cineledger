package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.DiscountNotValidException;
import bg.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import bg.vdrenkov.cineledger.exceptions.OrderNotFoundException;
import bg.vdrenkov.cineledger.mappers.OrderMapper;
import bg.vdrenkov.cineledger.models.entities.Item;
import bg.vdrenkov.cineledger.models.entities.Order;
import bg.vdrenkov.cineledger.models.entities.Ticket;
import bg.vdrenkov.cineledger.models.entities.User;
import bg.vdrenkov.cineledger.models.requests.OrderRequest;
import bg.vdrenkov.cineledger.repositories.OrderRepository;
import bg.vdrenkov.cineledger.testUtils.constants.DiscountConstants;
import bg.vdrenkov.cineledger.testUtils.constants.OrderConstants;
import bg.vdrenkov.cineledger.testUtils.constants.ReportConstants;
import bg.vdrenkov.cineledger.testUtils.factories.ItemFactory;
import bg.vdrenkov.cineledger.testUtils.factories.OrderFactory;
import bg.vdrenkov.cineledger.testUtils.factories.ProjectionFactory;
import bg.vdrenkov.cineledger.testUtils.factories.TicketFactory;
import bg.vdrenkov.cineledger.testUtils.factories.UserFactory;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import bg.vdrenkov.cineledger.models.dtos.OrderDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @Mock
  private OrderMapper orderMapper;

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

  @Test
  public void testAddOrder_noExceptions_success() throws MailjetSocketTimeoutException, MailjetException {
    Order expected = OrderFactory.getDefaultOrder();

    when(ticketService.getTicketById(anyInt())).thenReturn(TicketFactory.getDefaultTicket());
    when(itemService.getItemById(anyInt())).thenReturn(ItemFactory.getDefaultItem());
    when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
    when(orderRepository.save(any())).thenReturn(OrderFactory.getDefaultOrder());
    when(discountService.applyDiscount(anyDouble(), anyString())).thenReturn(OrderConstants.TOTAL_PRICE);

    Order order = orderService.addOrder(OrderFactory.getDefaultOrderRequest());

    assertEquals(expected, order);
    verify(emailService).sendOrderConfirmationEmail(any(), any());
  }

  @Test
  public void testAddOrder_discountCodeNotValid_throwsDiscountNotFoundException() throws MailjetSocketTimeoutException,
    MailjetException {
    assertThrows(DiscountNotValidException.class, () -> {

      when(ticketService.getTicketById(anyInt())).thenReturn(TicketFactory.getDefaultTicket());
      when(itemService.getItemById(anyInt())).thenReturn(ItemFactory.getDefaultItem());
      when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());

      OrderRequest orderRequest = OrderFactory.getDefaultOrderRequest();
      orderRequest.setDiscountCode("Code");
      orderService.addOrder(orderRequest);
    
    });
  }

  @Test
  public void testMakeReservationWithUserId_noExceptions_success() throws MailjetSocketTimeoutException,
    MailjetException {
    Order expected = OrderFactory.getDefaultOrder();
    Ticket ticket = new Ticket();
    ticket.setId(OrderConstants.ID);
    ticket.setDateOfPurchase(LocalDate.now());
    ticket.setProjection(ProjectionFactory.getDefaultProjection());

    when(ticketService.addTicket(any())).thenReturn(ticket);
    when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
    when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
    when(orderRepository.save(any())).thenReturn(expected);
    when(discountService.applyDiscount(anyDouble(), anyString())).thenReturn(OrderConstants.TOTAL_PRICE);

    Order order =
      orderService.makeReservationWithUserId(Collections.singletonList(TicketFactory.getDefaultTicketRequest()), OrderConstants.ID,
                                             DiscountConstants.CODE);

    assertEquals(expected, order);
  }

  @Test
  public void testMakeReservationWithUserId_discountCodeNotValid_throwsDiscountNotFoundException() throws
    MailjetSocketTimeoutException,
    MailjetException {
    assertThrows(DiscountNotValidException.class, () -> {

      Ticket ticket = new Ticket();
      ticket.setId(OrderConstants.ID);
      ticket.setDateOfPurchase(LocalDate.now());
      ticket.setProjection(ProjectionFactory.getDefaultProjection());

      String invalidDiscount = "ABC";

      when(ticketService.addTicket(any())).thenReturn(ticket);
      when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
      when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);

      orderService.makeReservationWithUserId(Collections.singletonList(TicketFactory.getDefaultTicketRequest()), OrderConstants.ID,
                                             invalidDiscount);
    
    });
  }

  @Test
  public void testMakeReservationWithUserId_invalidUserId_throwsNotAuthorizedException() throws
    MailjetSocketTimeoutException, MailjetException {
    assertThrows(NotAuthorizedException.class, () -> {

      when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

      orderService.makeReservationWithUserId(Collections.singletonList(TicketFactory.getDefaultTicketRequest()), OrderConstants.ID,
                                             null);
    
    });
  }

  @Test
  public void testGetOrdersByUserId_noExceptions_success() {
    User user = UserFactory.getDefaultUser();

    when(userService.getCurrentUser()).thenReturn(user);
    when(orderRepository.findOrderByUserId(anyInt())).thenReturn(OrderFactory.getDefaultOrderList());

    List<OrderDto> orderDtos = OrderFactory.getDefaultOrderDtoList();
    when(orderMapper.mapOrderToOrderDtoList(OrderFactory.getDefaultOrderList())).thenReturn(orderDtos);
    when(orderRepository.findOrderByUserId(anyInt())).thenReturn(OrderFactory.getDefaultOrderList());

    List<OrderDto> result = orderService.getOrdersByUserId(OrderConstants.ID);

    assertEquals(orderDtos, result);
  }

  @Test
  public void testGetOrdersByUserId_invalidUserId_throwsNotAuthorizedException() {
    assertThrows(NotAuthorizedException.class, () -> {

      User user = UserFactory.getDefaultUser();

      when(userService.getCurrentUser()).thenReturn(user);

      int invalidUserId = 999;

      orderService.getOrdersByUserId(invalidUserId);
    
    });
  }

  @Test
  public void testUpdateOrder_noExceptions_success() throws MailjetSocketTimeoutException, MailjetException {
    OrderDto expected = OrderFactory.getDefaultOrderDto();

    when(orderRepository.findById(anyInt())).thenReturn(Optional.of(OrderFactory.getDefaultOrder()));
    when(orderMapper.mapOrderToOrderDto(any())).thenReturn(OrderFactory.getDefaultOrderDto());
    when(ticketService.getTicketById(anyInt())).thenReturn(TicketFactory.getDefaultTicket());
    when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
    when(orderRepository.save(any())).thenReturn(OrderFactory.getDefaultOrder());

    Item newItem = ItemFactory.getDefaultItem();
    newItem.setId(999);
    when(itemService.getItemById(newItem.getId())).thenReturn(newItem);

    OrderRequest request = OrderFactory.getDefaultOrderRequest();
    request.setItemsIds(Collections.singletonList(newItem.getId()));
    OrderDto result = orderService.updateOrder(request, OrderConstants.ID);

    request.setItemsIds(new ArrayList<>());
    orderService.updateOrder(request, OrderConstants.ID);


    assertEquals(expected, result);
  }

  @Test
  public void testUpdateOrder_OrderNotFoundException_fail() throws MailjetSocketTimeoutException, MailjetException {
    assertThrows(OrderNotFoundException.class, () -> {

      when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

      orderService.updateOrder(OrderFactory.getDefaultOrderRequest(), OrderConstants.ID);
    
    });
  }

  @Test
  public void testDeleteOrder_noExceptions_fail() {
    OrderDto expected = OrderFactory.getDefaultOrderDto();

    when(orderRepository.findById(anyInt())).thenReturn(Optional.of(new Order()));
    when(orderMapper.mapOrderToOrderDto(any())).thenReturn(expected);

    OrderDto result = orderService.deleteOrder(OrderConstants.ID);

    assertEquals(expected, result);
  }

  @Test
  public void testDeleteOrder_OrderNotFoundException_fail() {
    assertThrows(OrderNotFoundException.class, () -> {

      when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

      orderService.deleteOrder(OrderConstants.ID);
    
    });
  }

  @Test
  public void testGetOrdersByDateBetween() {
    List<Order> expected = OrderFactory.getDefaultOrderList();

    when(orderRepository.findOrdersByDateOfPurchaseBetween(any(), any())).thenReturn(expected);

    List<Order> orders = orderService.getOrdersByDateBetween(ReportConstants.START_DATE, ReportConstants.END_DATE);

    assertEquals(expected, orders);
  }

  @Test
  public void testOrderMutations_areTransactional() throws NoSuchMethodException {
    Method addOrder = OrderService.class.getMethod("addOrder", OrderRequest.class);
    Method updateOrder = OrderService.class.getMethod("updateOrder", OrderRequest.class, int.class);
    Method reserveOrder = OrderService.class.getMethod(
      "makeReservationWithUserId", List.class, int.class, String.class);
    Method deleteOrder = OrderService.class.getMethod("deleteOrder", int.class);

    assertTrue(addOrder.isAnnotationPresent(Transactional.class));
    assertTrue(updateOrder.isAnnotationPresent(Transactional.class));
    assertTrue(reserveOrder.isAnnotationPresent(Transactional.class));
    assertTrue(deleteOrder.isAnnotationPresent(Transactional.class));
  }
}



