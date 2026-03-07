package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.CategoryService;
import dev.vdrenkov.cineledger.testutil.factories.CategoryFactory;
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

import static dev.vdrenkov.cineledger.testutil.constants.CategoryConstants.ID;
import static dev.vdrenkov.cineledger.testutil.constants.CategoryConstants.NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests category controller behavior.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class CategoryControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    /**
     * Verifies that add Category no Exceptions success.
     */
    @Test
    void testAddCategory_noExceptions_success() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(CategoryFactory.getDefaultCategoryRequest());

        when(categoryService.addCategory(any())).thenReturn(CategoryFactory.getDefaultCategory());

        mockMvc
            .perform(MockMvcRequestBuilders
                .post(URIConstants.CATEGORIES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", URIConstants.CATEGORIES_PATH + "/" + ID));
    }

    /**
     * Verifies that get All Categories single Category success.
     */
    @Test
    void testGetAllCategories_singleCategory_success() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(
            Collections.singletonList(CategoryFactory.getDefaultCategoryDto()));

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.CATEGORIES_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].name").value(NAME));
    }

    /**
     * Verifies that get All Categories empty List success.
     */
    @Test
    void testGetAllCategories_emptyList_success() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(
            Collections.singletonList(CategoryFactory.getDefaultCategoryDto()));

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.CATEGORIES_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].name").value(NAME));
    }

    /**
     * Verifies that get Category By Name category Found success.
     */
    @Test
    void testGetCategoryByName_categoryFound_success() throws Exception {
        when(categoryService.getCategoryDtoByName(anyString())).thenReturn(CategoryFactory.getDefaultCategoryDto());

        mockMvc
            .perform(MockMvcRequestBuilders.get(URIConstants.CATEGORIES_PATH).queryParam("categoryName", NAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.name").value(NAME));
    }

    /**
     * Verifies that update Category no Response success.
     */
    @Test
    void testUpdateCategory_noResponse_success() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(CategoryFactory.getDefaultCategoryRequest());

        when(categoryService.updateCategory(any(), eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

        mockMvc
            .perform(put(URIConstants.CATEGORIES_ID_PATH, ID).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that update Category requested Response success.
     */
    @Test
    void testUpdateCategory_requestedResponse_success() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(CategoryFactory.getDefaultCategoryRequest());

        when(categoryService.updateCategory(any(), eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

        mockMvc
            .perform(put(URIConstants.CATEGORIES_ID_PATH, ID)
                .queryParam("returnOld", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.name").value(NAME));
    }

    /**
     * Verifies that delete Category no Response success.
     */
    @Test
    void testDeleteCategory_noResponse_success() throws Exception {
        when(categoryService.deleteCategory(ID)).thenReturn(CategoryFactory.getDefaultCategoryDto());

        mockMvc.perform(delete(URIConstants.CATEGORIES_ID_PATH, ID)).andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete Category requested Response success.
     */
    @Test
    void testDeleteCategory_requestedResponse_success() throws Exception {
        when(categoryService.deleteCategory(ID)).thenReturn(CategoryFactory.getDefaultCategoryDto());

        mockMvc
            .perform(delete(URIConstants.CATEGORIES_ID_PATH, ID).queryParam("returnOld", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.name").value(NAME));
    }
}





