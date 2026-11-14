package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import dev.vdrenkov.cineledger.exceptions.OrderNotFoundException;
import dev.vdrenkov.cineledger.mappers.OrderMapper;
import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Item;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.entities.OrderItem;
import dev.vdrenkov.cineledger.models.entities.Ticket;
import dev.vdrenkov.cineledger.models.requests.OrderRequest;
import dev.vdrenkov.cineledger.models.requests.TicketRequest;
import dev.vdrenkov.cineledger.repositories.OrderItemRepository;
import dev.vdrenkov.cineledger.repositories.OrderRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {
    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private final DiscountService discountService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final TicketService ticketService;
    private final ItemService itemService;
    private final EmailService emailService;

    public OrderService(
            final DiscountService discountService,
            final OrderRepository orderRepository,
            final OrderItemRepository orderItemRepository,
            final UserService userService,
            final TicketService ticketService,
            final ItemService itemService,
            final EmailService emailService) {
        this.discountService = discountService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.ticketService = ticketService;
        this.itemService = itemService;
        this.emailService = emailService;
    }

    @Transactional
    public Order addOrder(final OrderRequest request) {
        final List<Ticket> tickets = lockTickets(request.ticketsIds());
        final var user = userService.getUserById(request.userId());
        final var order = new Order(LocalDate.now(), user, new ArrayList<>(tickets), ZERO);
        return saveOrder(order, tickets, request.itemsIds(), request.discountCode(), List.of());
    }

    @Transactional
    public Order makeReservationWithUserId(
            final List<TicketRequest> requests, final int userId, final String discountCode) {

        if (requests.isEmpty()) {
            throw new IllegalArgumentException("At least one ticket is required");
        }

        if (!userService.isCurrentUserAuthorized(userId)) {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        final var user = userService.getUserById(userId);
        final List<Ticket> tickets =
                requests.stream()
                        .sorted(Comparator.comparingInt(TicketRequest::projectionId))
                        .map(ticketService::addTicket)
                        .toList();
        final var order = new Order(LocalDate.now(), user, new ArrayList<>(tickets), ZERO);
        return saveOrder(order, tickets, List.of(), discountCode, List.of());
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(final int id) {
        final var order =
                orderRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        ExceptionMessages.ORDER_NOT_FOUND_MESSAGE));
        requireReadAccess(order.getUser().getId());
        return OrderMapper.mapOrderToOrderDto(order, orderItemRepository.findByOrderId(id));
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserId(final int userId) {
        requireReadAccess(userId);
        final var orders = orderRepository.findOrderByUserId(userId);
        final var items =
                orders.isEmpty()
                        ? Map.<Integer, List<OrderItem>>of()
                        : orderItemRepository
                                .findByOrderIdIn(orders.stream().map(Order::getId).toList())
                                .stream()
                                .collect(Collectors.groupingBy(OrderItem::getOrderId));
        return orders.stream()
                .map(order ->
                        OrderMapper.mapOrderToOrderDto(
                                order, items.getOrDefault(order.getId(), List.of())))
                .toList();
    }

    @Transactional
    public OrderDto updateOrder(final OrderRequest request, final int id) {
        final var order = lockOrder(id);
        final var previousItems = orderItemRepository.findByOrderId(id);
        final OrderDto previous = OrderMapper.mapOrderToOrderDto(order, previousItems);
        final List<Ticket> removed =
                order.getTickets().stream()
                        .filter(ticket -> !request.ticketsIds().contains(ticket.getId()))
                        .toList();
        final List<Ticket> tickets = lockTickets(request.ticketsIds());
        order.setUser(userService.getUserById(request.userId()));
        saveOrder(order, tickets, request.itemsIds(), request.discountCode(), previousItems);
        ticketService.cancelTickets(removed);
        return previous;
    }

    @Transactional
    public OrderDto deleteOrder(final int id) {
        final var order = lockOrder(id);
        final var items = orderItemRepository.findByOrderId(id);
        final OrderDto previous = OrderMapper.mapOrderToOrderDto(order, items);
        items.stream()
                .sorted(Comparator.comparingInt(OrderItem::getItemId))
                .forEach(line -> {
                    final Item item = itemService.getItemForUpdate(line.getItemId());
                    item.setQuantity(Math.addExact(item.getQuantity(), line.getQuantity()));
                });
        final List<Ticket> tickets = new ArrayList<>(order.getTickets());
        orderRepository.delete(order);
        orderRepository.flush();
        ticketService.cancelTickets(tickets);
        return previous;
    }

    private Order saveOrder(
            final Order order,
            final List<Ticket> tickets,
            final List<Integer> itemIds,
            final String discountCode,
            final List<OrderItem> previousItems) {
        final var quantities =
                (itemIds == null ? Stream.<Integer>empty() : itemIds.stream())
                        .collect(
                                Collectors.groupingBy(
                                        Function.identity(), TreeMap::new, Collectors.counting()));
        final var storedItems =
                Stream.concat(
                                quantities.keySet().stream(),
                                previousItems.stream().map(OrderItem::getItemId))
                        .distinct()
                        .sorted()
                        .collect(
                                Collectors.toMap(
                                        Function.identity(), itemService::getItemForUpdate));
        previousItems.forEach(line -> {
            final Item item = storedItems.get(line.getItemId());
            item.setQuantity(Math.addExact(item.getQuantity(), line.getQuantity()));
        });
        final var previousById =
                previousItems.stream()
                        .collect(Collectors.toMap(OrderItem::getItemId, Function.identity()));
        final List<ItemQuote> quotes =
                quantities.entrySet().stream()
                        .map(entry -> {
                            final Item item = storedItems.get(entry.getKey());
                            final int quantity = Math.toIntExact(entry.getValue());

                            if (item.getQuantity() < quantity) {
                                throw new dev.vdrenkov.cineledger.exceptions
                                        .NoAvailableItemsException(
                                        ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION);
                            }

                            item.setQuantity(item.getQuantity() - quantity);
                            final OrderItem previous = previousById.get(item.getId());
                            return new ItemQuote(
                                    item.getId(),
                                    previous == null ? item.getName() : previous.getName(),
                                    quantity,
                                    previous == null ? item.getPrice() : previous.getUnitPrice());
                        })
                        .toList();
        final int percentage = discountService.getDiscountPercentage(discountCode);
        final var gross =
                Stream.concat(
                                tickets.stream().map(Ticket::getUnitPrice),
                                quotes.stream()
                                        .map(quote ->
                                                quote.unitPrice()
                                                        .multiply(
                                                                BigDecimal.valueOf(
                                                                        quote.quantity()))))
                        .toList();
        final List<BigDecimal> paid = allocatePrices(gross, percentage);
        for (int index = 0; index < tickets.size(); index++)
            tickets.get(index).setPaidPrice(paid.get(index));
        order.setTickets(new ArrayList<>(tickets));
        order.setDiscountPercentage(percentage);
        order.setTotalPrice(paid.stream().reduce(ZERO, BigDecimal::add));
        final Order stored = orderRepository.saveAndFlush(order);
        orderItemRepository.deleteAll(
                previousItems.stream()
                        .filter(line -> !quantities.containsKey(line.getItemId()))
                        .toList());
        final List<OrderItem> lines = new ArrayList<>();
        for (int index = 0; index < quotes.size(); index++) {
            final ItemQuote quote = quotes.get(index);
            lines.add(
                    new OrderItem(
                            stored.getId(),
                            quote.itemId(),
                            quote.name(),
                            quote.quantity(),
                            quote.unitPrice(),
                            paid.get(tickets.size() + index)));
        }
        orderItemRepository.saveAll(lines);
        emailService.sendOrderConfirmationEmail(stored.getUser(), stored);
        return stored;
    }

    private List<Ticket> lockTickets(final List<Integer> ticketIds) {

        if (ticketIds.isEmpty() || ticketIds.stream().distinct().count() != ticketIds.size()) {
            throw new IllegalArgumentException("Tickets must be non-empty and must not repeat");
        }

        return ticketIds.stream().sorted().map(ticketService::getTicketForUpdate).toList();
    }

    private Order lockOrder(final int id) {
        return orderRepository
                .findByIdForUpdate(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND_MESSAGE));
    }

    private void requireReadAccess(final int userId) {
        final var currentUser = userService.getCurrentUser();
        final boolean staff =
                currentUser.getRoles().stream()
                        .anyMatch(role ->
                                "ADMIN".equals(role.getName()) || "VENDOR".equals(role.getName()));

        if (!staff && currentUser.getId() != userId) {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }
    }

    private static List<BigDecimal> allocatePrices(
            final List<BigDecimal> grossPrices, final int percentage) {
        final BigDecimal multiplier = BigDecimal.valueOf(100L - percentage).movePointLeft(2);
        BigDecimal cumulativeGross = ZERO;
        BigDecimal cumulativePaid = ZERO;
        final List<BigDecimal> allocated = new ArrayList<>();
        for (final BigDecimal price : grossPrices) {
            cumulativeGross = cumulativeGross.add(price);
            final BigDecimal nextPaid =
                    cumulativeGross.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
            allocated.add(nextPaid.subtract(cumulativePaid));
            cumulativePaid = nextPaid;
        }
        return allocated;
    }

    private record ItemQuote(int itemId, String name, int quantity, BigDecimal unitPrice) {}
}
