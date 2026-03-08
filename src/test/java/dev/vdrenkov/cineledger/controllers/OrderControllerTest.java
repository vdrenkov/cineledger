package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.services.OrderService;
import dev.vdrenkov.cineledger.testutils.factories.OrderFactory;
import dev.vdrenkov.cineledger.testutils.factories.TicketFactory;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.CODE;
import static dev.vdrenkov.cineledger.testutils.constants.RoleConstants.NAME;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.EMAIL;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.FIRST_NAME;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.JOIN_DATE;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.LAST_NAME;
import static dev.vdrenkov.cineledger.testutils.constants.UserConstants.USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests order controller behavior.
 */
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    private static final String RETURN_OLD = "returnOld";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    /**
     * Verifies that add Order order Added success.
     */
    @Test
    void testAddOrder_orderAdded_success() throws Exception {
        final String json = objectMapper.writeValueAsString(OrderFactory.getDefaultOrderRequest());
        when(orderService.addOrder(any())).thenReturn(OrderFactory.getDefaultOrder());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.ORDERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam("discountCode", CODE))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.ORDERS_PATH + "/" + ID));
    }

    /**
     * Verifies that make Reservation With User Id order Added success.
     */
    @Test
    void testMakeReservationWithUserId_orderAdded_success() throws Exception {
        String json = objectMapper.writeValueAsString(
            Collections.singletonList(TicketFactory.getDefaultTicketRequest()));
        when(orderService.makeReservationWithUserId(any(), anyInt(), anyString())).thenReturn(
            OrderFactory.getDefaultOrder());

        mockMvc
            .perform(post(URIConstants.USERS_ID_ORDERS_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.ORDERS_PATH + "/" + ID));
    }

    /**
     * Verifies that get Orders By User Id no Exceptions success.
     */
    @Test
    void testGetOrdersByUserId_noExceptions_success() throws Exception {
        final List<OrderDto> defaultOrderDtoList = OrderFactory.getDefaultOrderDtoList();
        final OrderDto orderDto = defaultOrderDtoList.getFirst();
        when(orderService.getOrdersByUserId(anyInt())).thenReturn(defaultOrderDtoList);

        mockMvc
            .perform(get(URIConstants.USERS_ID_ORDERS_PATH, ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(orderDto.getId()))
            .andExpect(jsonPath("$[0].user.username").value(USERNAME))
            .andExpect(jsonPath("$[0].user.email").value(EMAIL))
            .andExpect(jsonPath("$[0].user.firstName").value(FIRST_NAME))
            .andExpect(jsonPath("$[0].user.lastName").value(LAST_NAME))
            .andExpect(jsonPath("$[0].user.joinDate").value(JOIN_DATE.toString()))
            .andExpect(jsonPath("$[0].user.roles[0].name").value(NAME))
            .andExpect(jsonPath("$[0].tickets[0].id").value(orderDto.getTickets().getFirst().getId()))
            .andExpect(jsonPath("$[0].items[0]").value(orderDto.getItems().getFirst()))
            .andExpect(jsonPath("$[0].totalPrice").value(orderDto.getTotalPrice()));
    }

    /**
     * Verifies that update Order return Old True success.
     */
    @Test
    void testUpdateOrder_returnOldTrue_success() throws Exception {
        final OrderDto orderDto = OrderFactory.getDefaultOrderDto();
        when(orderService.updateOrder(any(), anyInt())).thenReturn(orderDto);
        final String json = objectMapper.writeValueAsString(OrderFactory.getDefaultOrderRequest());

        mockMvc
            .perform(put(URIConstants.ORDERS_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderDto.getId()))
            .andExpect(jsonPath("$.user.username").value(USERNAME))
            .andExpect(jsonPath("$.user.email").value(EMAIL))
            .andExpect(jsonPath("$.user.firstName").value(FIRST_NAME))
            .andExpect(jsonPath("$.user.lastName").value(LAST_NAME))
            .andExpect(jsonPath("$.user.joinDate").value(JOIN_DATE.toString()))
            .andExpect(jsonPath("$.user.roles[0].name").value(NAME))
            .andExpect(jsonPath("$.tickets[0].id").value(orderDto.getTickets().getFirst().getId()))
            .andExpect(jsonPath("$.items[0]").value(orderDto.getItems().getFirst()))
            .andExpect(jsonPath("$.totalPrice").value(orderDto.getTotalPrice()));
    }

    /**
     * Verifies that update Order return Old False success.
     */
    @Test
    void testUpdateOrder_returnOldFalse_success() throws Exception {
        when(orderService.updateOrder(any(), anyInt())).thenReturn(OrderFactory.getDefaultOrderDto());
        final String json = objectMapper.writeValueAsString(OrderFactory.getDefaultOrderRequest());

        mockMvc
            .perform(put(URIConstants.ORDERS_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete Order return Old True success.
     */
    @Test
    void testDeleteOrder_returnOldTrue_success() throws Exception {
        final OrderDto orderDto = OrderFactory.getDefaultOrderDto();
        when(orderService.deleteOrder(anyInt())).thenReturn(orderDto);

        mockMvc
            .perform(delete(URIConstants.ORDERS_ID_PATH, ID).queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderDto.getId()))
            .andExpect(jsonPath("$.user.username").value(USERNAME))
            .andExpect(jsonPath("$.user.email").value(EMAIL))
            .andExpect(jsonPath("$.user.firstName").value(FIRST_NAME))
            .andExpect(jsonPath("$.user.lastName").value(LAST_NAME))
            .andExpect(jsonPath("$.user.joinDate").value(JOIN_DATE.toString()))
            .andExpect(jsonPath("$.user.roles[0].name").value(NAME))
            .andExpect(jsonPath("$.tickets[0].id").value(orderDto.getTickets().getFirst().getId()))
            .andExpect(jsonPath("$.items[0]").value(orderDto.getItems().getFirst()))
            .andExpect(jsonPath("$.totalPrice").value(orderDto.getTotalPrice()));
    }

    /**
     * Verifies that delete Order return Old False success.
     */
    @Test
    void testDeleteOrder_returnOldFalse_success() throws Exception {
        when(orderService.deleteOrder(anyInt())).thenReturn(OrderFactory.getDefaultOrderDto());

        mockMvc.perform(delete(URIConstants.ORDERS_ID_PATH, ID)).andExpect(status().isNoContent());
    }
}



