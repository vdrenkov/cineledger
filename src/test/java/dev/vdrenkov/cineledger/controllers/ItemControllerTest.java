package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.ItemService;
import dev.vdrenkov.cineledger.testutils.factories.ItemFactory;
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

import static dev.vdrenkov.cineledger.testutils.constants.ItemConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.ItemConstants.IS_BELLOW;
import static dev.vdrenkov.cineledger.testutils.constants.ItemConstants.NAME;
import static dev.vdrenkov.cineledger.testutils.constants.ItemConstants.PRICE;
import static dev.vdrenkov.cineledger.testutils.constants.ItemConstants.QUANTITY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests item controller behavior.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    /**
     * Verifies that add Item no Exceptions success.
     */
    @Test
    void testAddItem_noExceptions_success() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(ItemFactory.getDefaultItemRequest());

        when(itemService.addItem(any())).thenReturn(ItemFactory.getDefaultItem());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.ITEMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.ITEMS_PATH + "/" + ID));
    }

    /**
     * Verifies that get All Items success.
     */
    @Test
    void testGetAllItems_success() throws Exception {
        when(itemService.getAllItems()).thenReturn(Collections.singletonList(ItemFactory.getDefaultItemDto()));

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.ITEMS_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].name").value(NAME))
            .andExpect(jsonPath("$[0].price").value(PRICE))
            .andExpect(jsonPath("$[0].quantity").value(QUANTITY));
    }

    /**
     * Verifies that get Item By Name item Found success.
     */
    @Test
    void testGetItemByName_itemFound_success() throws Exception {
        when(itemService.getItemDtoByName(anyString())).thenReturn(ItemFactory.getDefaultItemDto());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.ITEMS_PATH).queryParam("itemName", NAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.name").value(NAME));
    }

    /**
     * Verifies that edit Item no Response success.
     */
    @Test
    void testEditItem_noResponse_success() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(ItemFactory.getDefaultItemRequest());

        when(itemService.editItem(any(), eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

        mockMvc
            .perform(put(URIConstants.ITEMS_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that edit Item requested Response success.
     */
    @Test
    void testEditItem_requestedResponse_success() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(ItemFactory.getDefaultItemRequest());

        when(itemService.editItem(any(), eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

        mockMvc
            .perform(put(URIConstants.ITEMS_ID_PATH, ID)
                .queryParam("returnOld", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID));
    }

    /**
     * Verifies that delete Item no Response success.
     */
    @Test
    void testDeleteItem_noResponse_success() throws Exception {
        when(itemService.removeItem(ID)).thenReturn(ItemFactory.getDefaultItemDto());

        mockMvc.perform(delete(URIConstants.ITEMS_ID_PATH, ID)).andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete Item requested Response success.
     */
    @Test
    void testDeleteItem_requestedResponse_success() throws Exception {
        when(itemService.removeItem(ID)).thenReturn(ItemFactory.getDefaultItemDto());

        mockMvc
            .perform(delete(URIConstants.ITEMS_ID_PATH, ID).queryParam("returnOld", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID));
    }

    /**
     * Verifies that get Items By Quantity.
     */
    @Test
    void testGetItemsByQuantity() throws Exception {
        when(itemService.getItemsByQuantity(anyInt(), anyBoolean())).thenReturn(ItemFactory.getDefaultItemDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders
                .get(URIConstants.ITEMS_PATH)
                .queryParam("quantity", String.valueOf(QUANTITY))
                .queryParam("isBelow", String.valueOf(IS_BELLOW)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].name").value(NAME))
            .andExpect(jsonPath("$[0].price").value(PRICE))
            .andExpect(jsonPath("$[0].quantity").value(QUANTITY));
    }
}





