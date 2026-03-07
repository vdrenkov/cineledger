package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.testutil.constants.OrderConstants;
import dev.vdrenkov.cineledger.testutil.factories.ItemFactory;
import dev.vdrenkov.cineledger.testutil.factories.OrderFactory;
import dev.vdrenkov.cineledger.testutil.factories.TicketFactory;
import dev.vdrenkov.cineledger.testutil.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests order mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    @Mock
    private ItemMapper itemMapper;
    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private OrderMapper orderMapper;

    /**
     * Verifies that map Order To Order DTO success.
     */
    @Test
    void testMapOrderToOrderDto_success() {
        final Order order = OrderFactory.getDefaultOrder();
        final UserDto userDto = UserFactory.getDefaultUserDto();
        final List<TicketDto> ticketDtoList = TicketFactory.getDefaultTicketDtoList();
        final List<ItemDto> itemDtoList = ItemFactory.getDefaultItemDtoList();

        when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
        when(ticketMapper.mapTicketToDtoList(any())).thenReturn(ticketDtoList);
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(itemDtoList);

        final OrderDto orderDto = orderMapper.mapOrderToOrderDto(order);

        Assertions.assertEquals(OrderConstants.ID, orderDto.getId());
        assertEquals(userDto, orderDto.getUser());
        assertEquals(ticketDtoList, orderDto.getTickets());
        assertEquals(itemDtoList, orderDto.getItems());
        Assertions.assertEquals(OrderConstants.TOTAL_PRICE, orderDto.getTotalPrice(), 1.1);
        Assertions.assertEquals(OrderConstants.DATE_OF_PURCHASE, orderDto.getDateOfPurchase());
    }

    /**
     * Verifies that map Order To Order DTO List success.
     */
    @Test
    void testMapOrderToOrderDtoList_success() {
        final UserDto userDto = UserFactory.getDefaultUserDto();
        final List<TicketDto> ticketDtoList = TicketFactory.getDefaultTicketDtoList();
        final List<ItemDto> itemDtoList = ItemFactory.getDefaultItemDtoList();

        when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
        when(ticketMapper.mapTicketToDtoList(any())).thenReturn(ticketDtoList);
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(itemDtoList);

        final List<OrderDto> orderDtos = orderMapper.mapOrderToOrderDtoList(OrderFactory.getDefaultOrderList());
        final OrderDto orderDto = orderDtos.get(0);

        Assertions.assertEquals(OrderConstants.ID, orderDto.getId());
        assertEquals(userDto, orderDto.getUser());
        assertEquals(ticketDtoList, orderDto.getTickets());
        assertEquals(itemDtoList, orderDto.getItems());
        Assertions.assertEquals(OrderConstants.TOTAL_PRICE, orderDto.getTotalPrice(), 1.1);
        Assertions.assertEquals(OrderConstants.DATE_OF_PURCHASE, orderDto.getDateOfPurchase());
    }
}



