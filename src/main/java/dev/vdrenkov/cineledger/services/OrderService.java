package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.DiscountNotValidException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Contains business logic for order operations.
 */
@Service
public class OrderService {
    private final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final DiscountService discountService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final TicketService ticketService;
    private final ItemService itemService;
    private final EmailService emailService;

    /**
     * Creates a new order service with its required collaborators.
     *
     * @param discountService
     *     discount service used by the operation
     * @param orderRepository
     *     order repository used by the operation
     * @param userService
     *     user service used by the operation
     * @param ticketService
     *     ticket service used by the operation
     * @param itemService
     *     item service used by the operation
     * @param emailService
     *     email service used by the operation
     */
    @Autowired
    public OrderService(DiscountService discountService, OrderRepository orderRepository, UserService userService,
        TicketService ticketService, ItemService itemService, EmailService emailService) {
        this.discountService = discountService;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.ticketService = ticketService;
        this.itemService = itemService;
        this.emailService = emailService;
    }

    /**
     * Creates an order, applies pricing rules, and sends a confirmation email.
     *
     * @param request
     *     request payload containing the submitted data
     * @return requested order value
     */
    @Transactional(rollbackFor = Exception.class)
    public Order addOrder(final OrderRequest request) {
        List<Ticket> tickets = request
            .getTicketsIds()
            .stream()
            .map(ticketService::getTicketById)
            .collect(Collectors.toList());

        List<Item> items = new ArrayList<>();

        if (Objects.nonNull(request.getItemsIds()) && !request.getItemsIds().isEmpty()) {
            items = request.getItemsIds().stream().map(itemService::getItemById).collect(Collectors.toList());

            for (Item item : items) {
                itemService.decrementItemQuantity(item);
            }
        }

        final User user = userService.getUserById(request.getUserId());

        double price = calculateOrderPrice(items, tickets);

        final String discountCode = request.getDiscountCode();
        if (Objects.nonNull(discountCode) && isDiscountCodeValid(discountCode)) {
            price = discountService.applyDiscount(price, discountCode);
        }

        log.info("An attempt to add a new order in the database");

        Order order = new Order(LocalDate.now(), user, tickets, items, price);

        order = orderRepository.save(order);

        emailService.sendOrderConfirmationEmail(user, order);

        return order;
    }

    /**
     * Creates a reservation-only order for the specified user.
     *
     * @param requests
     *     request payloads to process
     * @param userId
     *     identifier of the target user
     * @param discountCode
     *     discount code to validate or apply
     * @return requested order value
     */
    @Transactional(rollbackFor = Exception.class)
    public Order makeReservationWithUserId(final List<TicketRequest> requests, final int userId,
        final String discountCode) {
        final List<Item> items = Collections.emptyList();
        final List<Ticket> tickets = new ArrayList<>();

        final User user = userService.getUserById(userId);

        if (!userService.isCurrentUserAuthorized(userId)) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.NOT_AUTHORIZED_MESSAGE));
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        for (TicketRequest request : requests) {
            tickets.add(ticketService.addTicket(new TicketRequest(request.getProjectionId())));
        }

        double price = calculateOrderPrice(items, tickets);

        if (Objects.nonNull(discountCode) && isDiscountCodeValid(discountCode)) {
            price = discountService.applyDiscount(price, discountCode);
        }

        log.info("An attempt to add a new order in the database");

        final Order order = orderRepository.save(new Order(LocalDate.now(), user, tickets, items, price));

        emailService.sendOrderConfirmationEmail(user, order);

        return order;
    }

    /**
     * Returns orders matching the supplied criteria.
     *
     * @param userId
     *     identifier of the target user
     * @return matching order dto values
     */
    public List<OrderDto> getOrdersByUserId(final int userId) {
        final User currentUser = userService.getCurrentUser();

        boolean isAdminOrVendor = currentUser
            .getRoles()
            .stream()
            .anyMatch(role -> role.getName().equals("ADMIN") || role.getName().equals("VENDOR"));

        if (!(isAdminOrVendor || currentUser.getId() == userId)) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.NOT_AUTHORIZED_MESSAGE));

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        log.info(String.format("All orders with user id %d were requested from the database", userId));

        return OrderMapper.mapOrderToOrderDtoList(orderRepository.findOrderByUserId(userId));
    }

    /**
     * Returns orders matching the supplied criteria.
     *
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return matching order values
     */
    public List<Order> getOrdersByDateBetween(final LocalDate startDate, final LocalDate endDate) {
        log.info(String.format("All orders with date between %s and %s were requested from the database", startDate,
            endDate));
        return orderRepository.findOrdersByDateOfPurchaseBetween(startDate, endDate);
    }

    /**
     * Updates order and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @return order dto result
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderDto updateOrder(final OrderRequest request, final int id) {
        final Order order = orderRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("Exception caught: %s", ExceptionMessages.ORDER_NOT_FOUND_MESSAGE));

            throw new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND_MESSAGE);
        });

        final OrderDto orderDto = OrderMapper.mapOrderToOrderDto(order);

        final List<Ticket> tickets = request
            .getTicketsIds()
            .stream()
            .map(ticketService::getTicketById)
            .collect(Collectors.toList());

        List<Item> items = new ArrayList<>();
        if (Objects.nonNull(request.getItemsIds()) && !request.getItemsIds().isEmpty()) {
            items = request.getItemsIds().stream().map(itemService::getItemById).collect(Collectors.toList());
        }

        final User user = userService.getUserById(request.getUserId());

        final double price = calculateOrderPrice(items, tickets);

        if (!items.isEmpty()) {
            final List<Item> existingItems = new ArrayList<>(order.getItems());

            for (Item item : items) {
                if (!existingItems.remove(item)) {
                    itemService.decrementItemQuantity(item);
                }
            }

            for (Item removedItem : existingItems) {
                itemService.incrementItemQuantity(removedItem);
            }
        } else {
            for (Item previousItem : order.getItems()) {
                itemService.incrementItemQuantity(previousItem);
            }
        }

        order.setItems(items);
        order.setTickets(tickets);
        order.setUser(user);
        order.setTotalPrice(price);

        orderRepository.save(order);

        log.info(String.format("Order with id %d was updated", id));

        emailService.sendOrderConfirmationEmail(user, order);

        return orderDto;
    }

    /**
     * Deletes order and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @return order dto result
     */
    @Transactional
    public OrderDto deleteOrder(final int id) {
        final Order order = orderRepository.findById(id).orElseThrow(() -> {
            log.error(String.format("Exception caught: %s", ExceptionMessages.ORDER_NOT_FOUND_MESSAGE));
            throw new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND_MESSAGE);
        });

        final OrderDto orderDto = OrderMapper.mapOrderToOrderDto(order);

        orderRepository.delete(order);
        log.info(String.format("Order with id %d was deleted from the database", id));
        return orderDto;
    }

    private boolean isDiscountCodeValid(final String discountCode) {
        final String regex = "\\d{4}";

        if (discountCode.matches(regex)) {
            return true;
        } else {
            throw new DiscountNotValidException(ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE);
        }
    }

    private double calculateOrderPrice(final List<Item> items, final List<Ticket> tickets) {
        double sum = 0;

        if (Objects.nonNull(items) && items.size() > 0) {
            for (Item item : items) {
                sum += item.getPrice();
            }
        }

        if (Objects.nonNull(tickets) && tickets.size() > 0) {
            for (Ticket ticket : tickets) {
                sum += ticket.getProjection().getPrice();
            }
        }
        return sum;
    }
}

