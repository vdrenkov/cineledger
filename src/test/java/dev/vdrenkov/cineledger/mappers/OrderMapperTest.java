package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.OrderDto;
import dev.vdrenkov.cineledger.models.entities.Order;
import dev.vdrenkov.cineledger.testutils.constants.OrderConstants;
import dev.vdrenkov.cineledger.testutils.factories.OrderFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    @Test
    void testMapOrderToOrderDto_success() {
        final Order order = OrderFactory.getDefaultOrder();

        final OrderDto orderDto = OrderMapper.mapOrderToOrderDto(order);

        assertEquals(OrderConstants.ID, orderDto.id());
        assertEquals(UserFactory.getDefaultUserDto(), orderDto.user());
        assertEquals(OrderConstants.TOTAL_PRICE, orderDto.totalPrice(), 1.1);
        assertEquals(OrderConstants.DATE_OF_PURCHASE, orderDto.dateOfPurchase());
    }

    @Test
    void testMapOrderToOrderDtoList_success() {
        final List<OrderDto> orderDtos = OrderMapper.mapOrderToOrderDtoList(OrderFactory.getDefaultOrderList());
        final OrderDto orderDto = orderDtos.getFirst();

        assertEquals(OrderConstants.ID, orderDto.id());
        assertEquals(UserFactory.getDefaultUserDto(), orderDto.user());
        assertEquals(OrderConstants.TOTAL_PRICE, orderDto.totalPrice(), 1.1);
        assertEquals(OrderConstants.DATE_OF_PURCHASE, orderDto.dateOfPurchase());
    }
}
