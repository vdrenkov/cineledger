package bg.vdrenkov.cineledger.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import bg.vdrenkov.cineledger.services.CategoryService;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
import tools.jackson.databind.ObjectMapper;
import bg.vdrenkov.cineledger.testUtils.factories.CategoryFactory;
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

import static bg.vdrenkov.cineledger.testUtils.constants.CategoryConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.CategoryConstants.NAME;
import static org.mockito.ArgumentMatchers.any;
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
public class CategoryControllerTest {

  private MockMvc mockMvc;

  @Mock
  private CategoryService categoryService;

  @InjectMocks
  private CategoryController categoryController;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(categoryController)
      .build();
  }

  @Test
  public void testAddCategory_noExceptions_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(CategoryFactory.getDefaultCategoryRequest());

    when(categoryService.addCategory(any())).thenReturn(CategoryFactory.getDefaultCategory());

    mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.CATEGORIES_PATH)
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", URIConstants.CATEGORIES_PATH + "/" + ID));
  }

  @Test
  public void testGetAllCategories_singleCategory_success() throws Exception {
    when(categoryService.getAllCategories()).thenReturn(
      Collections.singletonList(CategoryFactory.getDefaultCategoryDto()));

    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.CATEGORIES_PATH))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].name").value(NAME));
  }

  @Test
  public void testGetAllCategories_emptyList_success() throws Exception {
    when(categoryService.getAllCategories()).thenReturn(
      Collections.singletonList(CategoryFactory.getDefaultCategoryDto()));

    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.CATEGORIES_PATH))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").exists())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].name").value(NAME));
  }

  @Test
  public void testGetCategoryByName_categoryFound_success() throws Exception {
    when(categoryService.getCategoryDtoByName(anyString())).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.CATEGORIES_PATH)
                                          .queryParam("categoryName", NAME))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").exists())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.name").value(NAME));
  }

  @Test
  public void testUpdateCategory_noResponse_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(CategoryFactory.getDefaultCategoryRequest());

    when(categoryService.updateCategory(any(), eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(put(URIConstants.CATEGORIES_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testUpdateCategory_requestedResponse_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(CategoryFactory.getDefaultCategoryRequest());

    when(categoryService.updateCategory(any(), eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(put(URIConstants.CATEGORIES_ID_PATH, ID)
                      .queryParam("returnOld", "true")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.name").value(NAME));
  }

  @Test
  public void testDeleteCategory_noResponse_success() throws Exception {
    when(categoryService.deleteCategory(eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(delete(URIConstants.CATEGORIES_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteCategory_requestedResponse_success() throws Exception {
    when(categoryService.deleteCategory(eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(delete(URIConstants.CATEGORIES_ID_PATH, ID)
                      .queryParam("returnOld", "true"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.name").value(NAME));
  }
}





