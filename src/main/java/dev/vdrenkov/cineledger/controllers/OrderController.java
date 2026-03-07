package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.models.requests.OrderRequest;
import dev.vdrenkov.cineledger.models.requests.TicketRequest;
import dev.vdrenkov.cineledger.services.OrderService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Exposes REST endpoints for managing order data.
 */
@RestController
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    /**
     * Creates a new order controller with its required collaborators.
     *
     * @param orderService
     *     order service used by the operation
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates and persists order.
     *
     * @param request
     *     request payload containing the submitted data
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.ORDERS_PATH)
    public ResponseEntity<Void> addOrder(@RequestBody @Valid OrderRequest request) {

        final Order order = orderService.addOrder(request);
        log.info("A request for an order to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.ORDERS_ID_PATH)
            .buildAndExpand(order.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Executes the make reservation with user id operation for order.
     *
     * @param requests
     *     request payloads to process
     * @param id
     *     identifier of the target resource
     * @param false
     *     false used by the operation
     * @param discountCode
     *     discount code to validate or apply
     * @return HTTP response describing the operation result
     */
    @PostMapping(URIConstants.USERS_ID_ORDERS_PATH)
    public ResponseEntity<Void> makeReservationWithUserId(@RequestBody @Valid List<TicketRequest> requests,
        @PathVariable int id, @RequestParam(required = false, defaultValue = "Code") String discountCode) {

        final Order order = orderService.makeReservationWithUserId(requests, id, discountCode);
        log.info("A request for a ticket to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.ORDERS_ID_PATH)
            .buildAndExpand(order.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Returns orders matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.USERS_ID_ORDERS_PATH)
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable int id) {
        final List<OrderDto> orderDtos = orderService.getOrdersByUserId(id);
        log.info(String.format("All orders by user id %d were requested from the database", id));

        return ResponseEntity.ok(orderDtos);
    }

    /**
     * Updates order and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @PutMapping(URIConstants.ORDERS_ID_PATH)
    public ResponseEntity<OrderDto> updateOrder(@RequestBody @Valid OrderRequest request, @PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        final OrderDto orderDto = orderService.updateOrder(request, id);
        log.info(String.format("Order with an id %d was updated", id));

        return returnOld ? ResponseEntity.ok(orderDto) : ResponseEntity.noContent().build();
    }

    /**
     * Deletes order and returns the removed state when needed.
     *
     * @param id
     *     identifier of the target resource
     * @param returnOld
     *     whether the previous persisted state should be returned
     * @return HTTP response describing the operation result
     */
    @DeleteMapping(URIConstants.ORDERS_ID_PATH)
    public ResponseEntity<OrderDto> deleteOrder(@PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {
        final OrderDto orderDto = orderService.deleteOrder(id);
        log.info(String.format("Order with an id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(orderDto) : ResponseEntity.noContent().build();
    }
}


