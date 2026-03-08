package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.DiscountService;
import dev.vdrenkov.cineledger.testutils.factories.DiscountFactory;
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

import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.CODE;
import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.PERCENTAGE;
import static dev.vdrenkov.cineledger.testutils.constants.DiscountConstants.TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests discount controller behavior.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class DiscountControllerTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private DiscountController discountController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(discountController).build();
    }

    /**
     * Verifies that add Discount no Exceptions success.
     */
    @Test
    void testAddDiscount_noExceptions_success() throws Exception {
        final String json = mapper.writeValueAsString(DiscountFactory.getDefaultDiscountRequest());

        when(discountService.addDiscount(any())).thenReturn(DiscountFactory.getDefaultDiscount());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.DISCOUNTS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.DISCOUNTS_PATH + "/" + ID));
    }

    /**
     * Verifies that get All Discounts single Discount success.
     */
    @Test
    void testGetAllDiscounts_singleDiscount_success() throws Exception {
        when(discountService.getAllDiscountDtos()).thenReturn(DiscountFactory.getDefaultDiscountDtoList());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.DISCOUNTS_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].type").value(TYPE))
            .andExpect(jsonPath("$[0].code").value(CODE))
            .andExpect(jsonPath("$[0].percentage").value(PERCENTAGE));
    }

    /**
     * Verifies that get Discount By Type discount Found success.
     */
    @Test
    void testGetDiscountByType_discountFound_success() throws Exception {
        when(discountService.getDiscountDtoByType(anyString())).thenReturn(DiscountFactory.getDefaultDiscountDto());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.DISCOUNTS_PATH).queryParam("type", TYPE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.type").value(TYPE))
            .andExpect(jsonPath("$.code").value(CODE))
            .andExpect(jsonPath("$.percentage").value(PERCENTAGE));
    }

    /**
     * Verifies that update Discount discount Updated returns Ok.
     */
    @Test
    void testUpdateDiscount_discountUpdated_returnsOk() throws Exception {
        final String json = mapper.writeValueAsString(DiscountFactory.getDefaultDiscountRequest());
        when(discountService.updateDiscount(any(), anyInt())).thenReturn(DiscountFactory.getDefaultDiscountDto());

        mockMvc
            .perform(put(URIConstants.DISCOUNTS_ID_PATH, ID)
                .queryParam("returnOld", String.valueOf(true))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.type").value(TYPE))
            .andExpect(jsonPath("$.code").value(CODE))
            .andExpect(jsonPath("$.percentage").value(PERCENTAGE));
    }

    /**
     * Verifies that update Discount discount Updated returns No Content.
     */
    @Test
    void testUpdateDiscount_discountUpdated_returnsNoContent() throws Exception {
        final String json = mapper.writeValueAsString(DiscountFactory.getDefaultDiscountRequest());
        when(discountService.updateDiscount(any(), anyInt())).thenReturn(DiscountFactory.getDefaultDiscountDto());

        mockMvc
            .perform(put(URIConstants.DISCOUNTS_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete Discount discount Deleted returns Ok.
     */
    @Test
    void testDeleteDiscount_discountDeleted_returnsOk() throws Exception {
        when(discountService.deleteDiscount(anyInt())).thenReturn(DiscountFactory.getDefaultDiscountDto());

        mockMvc
            .perform(delete(URIConstants.DISCOUNTS_ID_PATH, ID).queryParam("returnOld", String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.type").value(TYPE))
            .andExpect(jsonPath("$.code").value(CODE))
            .andExpect(jsonPath("$.percentage").value(PERCENTAGE));
    }

    /**
     * Verifies that delete Discount discount Deleted returns No Content.
     */
    @Test
    void testDeleteDiscount_discountDeleted_returnsNoContent() throws Exception {
        when(discountService.deleteDiscount(anyInt())).thenReturn(DiscountFactory.getDefaultDiscountDto());

        mockMvc.perform(delete(URIConstants.DISCOUNTS_ID_PATH, ID)).andExpect(status().isNoContent());
    }
}





