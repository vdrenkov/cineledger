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

import static org.junit.Assert.assertEquals;

/**
 * Tests order mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class OrderMapperTest {
    /**
     * Verifies that map Order To Order DTO success.
     */
    @Test
    void testMapOrderToOrderDto_success() {
        final Order order = OrderFactory.getDefaultOrder();

        final OrderDto orderDto = OrderMapper.mapOrderToOrderDto(order);

        assertEquals(OrderConstants.ID, orderDto.getId());
        assertEquals(UserFactory.getDefaultUserDto(), orderDto.getUser());
        assertEquals(OrderConstants.TOTAL_PRICE, orderDto.getTotalPrice(), 1.1);
        assertEquals(OrderConstants.DATE_OF_PURCHASE, orderDto.getDateOfPurchase());
    }

    /**
     * Verifies that map Order To Order DTO List success.
     */
    @Test
    void testMapOrderToOrderDtoList_success() {
        final List<OrderDto> orderDtos = OrderMapper.mapOrderToOrderDtoList(OrderFactory.getDefaultOrderList());
        final OrderDto orderDto = orderDtos.getFirst();

        assertEquals(OrderConstants.ID, orderDto.getId());
        assertEquals(UserFactory.getDefaultUserDto(), orderDto.getUser());
        assertEquals(OrderConstants.TOTAL_PRICE, orderDto.getTotalPrice(), 1.1);
        assertEquals(OrderConstants.DATE_OF_PURCHASE, orderDto.getDateOfPurchase());
    }
}



