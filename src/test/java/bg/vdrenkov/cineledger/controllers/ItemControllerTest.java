package bg.vdrenkov.cineledger.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import bg.vdrenkov.cineledger.services.ItemService;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
import tools.jackson.databind.ObjectMapper;
import bg.vdrenkov.cineledger.testUtils.factories.ItemFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static bg.vdrenkov.cineledger.testUtils.constants.ItemConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.ItemConstants.IS_BELLOW;
import static bg.vdrenkov.cineledger.testUtils.constants.ItemConstants.NAME;
import static bg.vdrenkov.cineledger.testUtils.constants.ItemConstants.PRICE;
import static bg.vdrenkov.cineledger.testUtils.constants.ItemConstants.QUANTITY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ItemControllerTest {

  private MockMvc mockMvc;

  @Mock
  private ItemService itemService;
  @InjectMocks
  private ItemController itemController;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(itemController)
      .build();
  }

  @Test
  public void testAddItem_noExceptions_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(ItemFactory.getDefaultItemRequest());

    when(itemService.addItem(any())).thenReturn(ItemFactory.getDefaultItem());

    mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.ITEMS_PATH)
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", URIConstants.ITEMS_PATH + "/" + ID));
  }

  @Test
  public void testGetAllItems_success() throws Exception {
    when(itemService.getAllItems()).thenReturn(
      Collections.singletonList(ItemFactory.getDefaultItemDto()));

    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.ITEMS_PATH))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").exists())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].name").value(NAME))
           .andExpect(jsonPath("$[0].price").value(PRICE))
           .andExpect(jsonPath("$[0].quantity").value(QUANTITY));
  }

  @Test
  public void testGetItemByName_itemFound_success() throws Exception {
    when(itemService.getItemDtoByName(anyString())).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.ITEMS_PATH)
                                          .queryParam("itemName", NAME))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").exists())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.name").value(NAME));
  }

  @Test
  public void testEditItem_noResponse_sucess() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(ItemFactory.getDefaultItemRequest());

    when(itemService.editItem(any(), eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(put(URIConstants.ITEMS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testEditItem_requestedResponse_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(ItemFactory.getDefaultItemRequest());

    when(itemService.editItem(any(), eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(put(URIConstants.ITEMS_ID_PATH, ID)
                      .queryParam("returnOld", "true")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID));
  }

  @Test
  public void testDeleteItem_noResponse_success() throws Exception {
    when(itemService.removeItem(eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(delete(URIConstants.ITEMS_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteItem_requestedResponse_success() throws Exception {
    when(itemService.removeItem(eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(delete(URIConstants.ITEMS_ID_PATH, ID)
                      .queryParam("returnOld", "true"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID));
  }

  @Test
  public void testGetItemsByQuantity() throws Exception {
    when(itemService.getItemsByQuantity(anyInt(), anyBoolean())).thenReturn(ItemFactory.getDefaultItemDtoList());


    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.ITEMS_PATH)
                                          .queryParam("quantity", String.valueOf(QUANTITY))
                                          .queryParam("isBelow", String.valueOf(IS_BELLOW)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].name").value(NAME))
           .andExpect(jsonPath("$[0].price").value(PRICE))
           .andExpect(jsonPath("$[0].quantity").value(QUANTITY));
  }
}





