package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.CategoryAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CategoryNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import dev.vdrenkov.cineledger.models.requests.CategoryRequest;
import dev.vdrenkov.cineledger.repositories.CategoryRepository;
import dev.vdrenkov.cineledger.testutils.constants.CategoryConstants;
import dev.vdrenkov.cineledger.testutils.factories.CategoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    final CategoryRequest categoryRequest = CategoryFactory.getDefaultCategoryRequest();

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testAddCategory_noExceptions_success() {
        final Category expected = CategoryFactory.getDefaultCategory();
        when(categoryRepository.save(any())).thenReturn(expected);

        final Category category = categoryService.addCategory(categoryRequest);

        assertEquals(expected, category);
    }

    @Test
    void testGetAllCategories_noExceptions_success() {
        final List<CategoryDto> expected = CategoryFactory.getDefaultCategoryDtoList();

        when(categoryRepository.findAll()).thenReturn(CategoryFactory.getDefaultCategoryList());

        final List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(expected, result);
    }

    @Test
    void testGetCategoryDtoById_noExceptions_success() {
        final CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));

        final CategoryDto categoryDto = categoryService.getCategoryDtoById(CategoryConstants.ID);

        assertEquals(expected, categoryDto);
    }

    @Test
    void testGetCategoryDtoById_throwsCategoryNotFoundException() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryDtoById(CategoryConstants.ID));
    }

    @Test
    void testGetCategoryDtoByName_noExceptions_success() {
        final CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));

        final CategoryDto categoryDto = categoryService.getCategoryDtoByName(CategoryConstants.NAME);

        assertEquals(expected, categoryDto);
    }

    @Test
    void testGetCategoryDtoByName_throwsCategoryNotFoundException() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
            () -> categoryService.getCategoryDtoByName(CategoryConstants.NAME));
    }

    @Test
    void testUpdateCategory_success() {
        final CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));
        when(categoryRepository.save(any())).thenReturn(CategoryFactory.getDefaultCategory());

        final CategoryDto categoryDto = categoryService.updateCategory(categoryRequest, CategoryConstants.ID);

        assertEquals(expected, categoryDto);
    }

    @Test
    void testUpdateCategory_categoryExists_throws() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category()));

        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.addCategory(categoryRequest));
    }

    @Test
    void testDeleteCategory_success() {
        final CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));

        final CategoryDto categoryDto = categoryService.deleteCategory(CategoryConstants.ID);

        assertEquals(expected, categoryDto);
    }

    @Test
    void testGetCategoryById_categoryNotFound_throwsCategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(CategoryConstants.ID));
    }
}
