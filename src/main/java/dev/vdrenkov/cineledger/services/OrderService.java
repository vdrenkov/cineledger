package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import dev.vdrenkov.cineledger.exceptions.OrderNotFoundException;
import dev.vdrenkov.cineledger.mappers.OrderMapper;
import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.models.requests.OrderRequest;
import dev.vdrenkov.cineledger.models.requests.TicketRequest;
import dev.vdrenkov.cineledger.repositories.OrderRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private final DiscountService discountService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final TicketService ticketService;
    private final ItemService itemService;
    private final EmailService emailService;

    public OrderService(
            DiscountService discountService,
            OrderRepository orderRepository,
            UserService userService,
            TicketService ticketService,
            ItemService itemService,
            EmailService emailService) {
        this.discountService = discountService;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.ticketService = ticketService;
        this.itemService = itemService;
        this.emailService = emailService;
    }

    @Transactional(rollbackFor = Exception.class)
    public Order addOrder(final OrderRequest request) {
        validateTickets(request.ticketsIds());
        final List<Ticket> tickets =
                request.ticketsIds().stream()
                        .sorted()
                        .map(ticketService::getTicketForUpdate)
                        .toList();

        List<Item> items = new ArrayList<>();

        if (request.itemsIds() != null && !request.itemsIds().isEmpty()) {
            items =
                    request.itemsIds().stream()
                            .sorted()
                            .map(itemService::getItemForUpdate)
                            .toList();

            for (Item item : items) {
                ItemService.decrementItemQuantity(item);
            }
        }

        final User user = userService.getUserById(request.userId());

        double price = calculateOrderPrice(items, tickets);

        final String discountCode = request.discountCode();
        if (discountCode != null && !discountCode.isBlank()) {
            price = discountService.applyDiscount(price, discountCode);
        }

        Order order = new Order(LocalDate.now(), user, tickets, items, price);

        order = orderRepository.save(order);

        emailService.sendOrderConfirmationEmail(user, order);

        return order;
    }

    @Transactional(rollbackFor = Exception.class)
    public Order makeReservationWithUserId(
            final List<TicketRequest> requests, final int userId, final String discountCode) {
        if (requests.isEmpty()) {
            throw new IllegalArgumentException("At least one ticket is required");
        }

        final List<Item> items = Collections.emptyList();
        final List<Ticket> tickets = new ArrayList<>();

        final User user = userService.getUserById(userId);

        if (!userService.isCurrentUserAuthorized(userId)) {

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        for (TicketRequest request :
                requests.stream()
                        .sorted(java.util.Comparator.comparingInt(TicketRequest::projectionId))
                        .toList()) {
            tickets.add(ticketService.addTicket(new TicketRequest(request.projectionId())));
        }

        double price = calculateOrderPrice(items, tickets);

        if (discountCode != null && !discountCode.isBlank()) {
            price = discountService.applyDiscount(price, discountCode);
        }

        final Order order =
                orderRepository.save(new Order(LocalDate.now(), user, tickets, items, price));

        emailService.sendOrderConfirmationEmail(user, order);

        return order;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserId(final int userId) {
        final User currentUser = userService.getCurrentUser();

        boolean isAdminOrVendor =
                currentUser.getRoles().stream()
                        .anyMatch(role ->
                                "ADMIN".equals(role.getName()) || "VENDOR".equals(role.getName()));

        if (!(isAdminOrVendor || currentUser.getId() == userId)) {

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        return OrderMapper.mapOrderToOrderDtoList(orderRepository.findOrderByUserId(userId));
    }

    public List<Order> getOrdersByDateBetween(final LocalDate startDate, final LocalDate endDate) {

        return orderRepository.findOrdersByDateOfPurchaseBetween(startDate, endDate);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDto updateOrder(final OrderRequest request, final int id) {
        final Order order =
                orderRepository
                        .findByIdForUpdate(id)
                        .orElseThrow(() -> {
                            return new OrderNotFoundException(
                                    ExceptionMessages.ORDER_NOT_FOUND_MESSAGE);
                        });

        final OrderDto orderDto = OrderMapper.mapOrderToOrderDto(order);
        java.util.stream.Stream.concat(
                        order.getItems().stream().map(Item::getId),
                        request.itemsIds() == null
                                ? java.util.stream.Stream.empty()
                                : request.itemsIds().stream())
                .distinct()
                .sorted()
                .forEach(itemService::getItemForUpdate);
        final List<Ticket> removedTickets = new ArrayList<>(order.getTickets());
        removedTickets.removeIf(ticket -> request.ticketsIds().contains(ticket.getId()));

        validateTickets(request.ticketsIds());
        final List<Ticket> tickets =
                request.ticketsIds().stream()
                        .sorted()
                        .map(ticketService::getTicketForUpdate)
                        .toList();

        List<Item> items = new ArrayList<>();
        if (Objects.nonNull(request.itemsIds()) && !request.itemsIds().isEmpty()) {
            items =
                    request.itemsIds().stream()
                            .sorted()
                            .map(itemService::getItemForUpdate)
                            .toList();
        }

        final User user = userService.getUserById(request.userId());

        final double price = calculateOrderPrice(items, tickets);

        if (!items.isEmpty()) {
            final List<Item> existingItems = new ArrayList<>(order.getItems());

            for (Item item : items) {
                if (!existingItems.remove(item)) {
                    ItemService.decrementItemQuantity(item);
                }
            }

            for (Item removedItem : existingItems) {
                ItemService.incrementItemQuantity(removedItem);
            }
        } else {
            for (Item previousItem : order.getItems()) {
                ItemService.incrementItemQuantity(previousItem);
            }
        }

        order.setItems(items);
        order.setTickets(tickets);
        order.setUser(user);
        order.setTotalPrice(price);

        orderRepository.saveAndFlush(order);
        ticketService.cancelTickets(removedTickets);

        emailService.sendOrderConfirmationEmail(user, order);

        return orderDto;
    }

    @Transactional
    public OrderDto deleteOrder(final int id) {
        final Order order =
                orderRepository
                        .findByIdForUpdate(id)
                        .orElseThrow(() -> {
                            return new OrderNotFoundException(
                                    ExceptionMessages.ORDER_NOT_FOUND_MESSAGE);
                        });

        final OrderDto orderDto = OrderMapper.mapOrderToOrderDto(order);

        final List<Ticket> tickets = new ArrayList<>(order.getTickets());
        order.getItems().stream()
                .map(Item::getId)
                .distinct()
                .sorted()
                .forEach(itemService::getItemForUpdate);
        order.getItems().forEach(ItemService::incrementItemQuantity);
        orderRepository.delete(order);
        orderRepository.flush();
        ticketService.cancelTickets(tickets);

        return orderDto;
    }

    private static void validateTickets(final List<Integer> ticketIds) {

        if (ticketIds.isEmpty() || ticketIds.stream().distinct().count() != ticketIds.size()) {
            throw new IllegalArgumentException("Tickets must be non-empty and must not repeat");
        }
    }

    private static double calculateOrderPrice(final List<Item> items, final List<Ticket> tickets) {
        double sum = 0;

        if (Objects.nonNull(items) && !items.isEmpty()) {
            for (Item item : items) {
                sum += item.getPrice();
            }
        }

        if (Objects.nonNull(tickets) && !tickets.isEmpty()) {
            for (Ticket ticket : tickets) {
                sum += ticket.getProjection().getPrice();
            }
        }
        return sum;
    }
}
