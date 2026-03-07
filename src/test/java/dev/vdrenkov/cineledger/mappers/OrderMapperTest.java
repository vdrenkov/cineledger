package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ItemDto;
import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.dtos.TicketDto;
import dev.vdrenkov.cineledger.models.dtos.UserDto;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.testUtils.constants.OrderConstants;
import dev.vdrenkov.cineledger.testUtils.factories.ItemFactory;
import dev.vdrenkov.cineledger.testUtils.factories.OrderFactory;
import dev.vdrenkov.cineledger.testUtils.factories.TicketFactory;
import dev.vdrenkov.cineledger.testUtils.factories.UserFactory;
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

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

    @Mock
    private ItemMapper itemMapper;
    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private OrderMapper orderMapper;

    @Test
    public void testMapOrderToOrderDto_success() {
        Order order = OrderFactory.getDefaultOrder();
        UserDto userDto = UserFactory.getDefaultUserDto();
        List<TicketDto> ticketDtoList = TicketFactory.getDefaultTicketDtoList();
        List<ItemDto> itemDtoList = ItemFactory.getDefaultItemDtoList();

        when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
        when(ticketMapper.mapTicketToDtoList(any())).thenReturn(ticketDtoList);
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(itemDtoList);

        OrderDto orderDto = orderMapper.mapOrderToOrderDto(order);

        Assertions.assertEquals(OrderConstants.ID, orderDto.getId());
        assertEquals(userDto, orderDto.getUser());
        assertEquals(ticketDtoList, orderDto.getTickets());
        assertEquals(itemDtoList, orderDto.getItems());
        Assertions.assertEquals(OrderConstants.TOTAL_PRICE, orderDto.getTotalPrice(), 1.1);
        Assertions.assertEquals(OrderConstants.DATE_OF_PURCHASE, orderDto.getDateOfPurchase());
    }

    @Test
    public void testMapOrderToOrderDtoList_success() {
        UserDto userDto = UserFactory.getDefaultUserDto();
        List<TicketDto> ticketDtoList = TicketFactory.getDefaultTicketDtoList();
        List<ItemDto> itemDtoList = ItemFactory.getDefaultItemDtoList();

        when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
        when(ticketMapper.mapTicketToDtoList(any())).thenReturn(ticketDtoList);
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(itemDtoList);

        List<OrderDto> orderDtos = orderMapper.mapOrderToOrderDtoList(OrderFactory.getDefaultOrderList());
        OrderDto orderDto = orderDtos.get(0);

        Assertions.assertEquals(OrderConstants.ID, orderDto.getId());
        assertEquals(userDto, orderDto.getUser());
        assertEquals(ticketDtoList, orderDto.getTickets());
        assertEquals(itemDtoList, orderDto.getItems());
        Assertions.assertEquals(OrderConstants.TOTAL_PRICE, orderDto.getTotalPrice(), 1.1);
        Assertions.assertEquals(OrderConstants.DATE_OF_PURCHASE, orderDto.getDateOfPurchase());
    }
}



