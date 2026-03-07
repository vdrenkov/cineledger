package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.entities.Order;
import bg.vdrenkov.cineledger.models.requests.OrderRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import bg.vdrenkov.cineledger.models.dtos.OrderDto;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.DiscountConstants.CODE;
import static bg.vdrenkov.cineledger.testUtils.constants.OrderConstants.DATE_OF_PURCHASE;
import static bg.vdrenkov.cineledger.testUtils.constants.OrderConstants.TOTAL_PRICE;
import static bg.vdrenkov.cineledger.testUtils.constants.OrderConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.factories.ItemFactory.getDefaultItemDtoList;
import static bg.vdrenkov.cineledger.testUtils.factories.ItemFactory.getDefaultItemList;

public final class OrderFactory {

  private OrderFactory() throws IllegalAccessException {
    throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static OrderRequest getDefaultOrderRequest() {
    return new OrderRequest(ID, TicketFactory.getDefaultIdList(), TicketFactory.getDefaultIdList(), CODE);
  }

  public static Order getDefaultOrder() {

    return new Order(ID, DATE_OF_PURCHASE, UserFactory.getDefaultUser(), TicketFactory.getDefaultTicketList(),
                     getDefaultItemList(),
                     TOTAL_PRICE);
  }

  public static List<Order> getDefaultOrderList() {
    return Collections.singletonList(getDefaultOrder());
  }

  public static OrderDto getDefaultOrderDto() {
    return new OrderDto(ID, DATE_OF_PURCHASE, UserFactory.getDefaultUserDto(), TicketFactory.getDefaultTicketDtoList(),
                        getDefaultItemDtoList(),
                        TOTAL_PRICE);
  }

  public static List<OrderDto> getDefaultOrderDtoList() {
    return Collections.singletonList(getDefaultOrderDto());
  }
}


