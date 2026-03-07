package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.CategoryAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CategoryNotFoundException;
import dev.vdrenkov.cineledger.mappers.CategoryMapper;
import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
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

/**
 * Tests category service behavior.
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    /**
     * Verifies that add Category no Exceptions success.
     */
    @Test
    void testAddCategory_noExceptions_success() {
        final Category expected = CategoryFactory.getDefaultCategory();
        when(categoryRepository.save(any())).thenReturn(expected);

        final Category category = categoryService.addCategory(CategoryFactory.getDefaultCategoryRequest());

        assertEquals(expected, category);
    }

    /**
     * Verifies that get All Categories no Exceptions success.
     */
    @Test
    void testGetAllCategories_noExceptions_success() {
        final List<CategoryDto> expected = CategoryFactory.getDefaultCategoryDtoList();

        when(categoryMapper.mapCategoryToCategoryDtoList(any())).thenReturn(expected);
        when(categoryRepository.findAll()).thenReturn(CategoryFactory.getDefaultCategoryList());

        final List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(expected, result);
    }

    /**
     * Verifies that get Category DTO By Id no Exceptions success.
     */
    @Test
    void testGetCategoryDtoById_noExceptions_success() {
        final CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));

        final CategoryDto categoryDto = categoryService.getCategoryDtoById(CategoryConstants.ID);

        assertEquals(expected, categoryDto);
    }

    /**
     * Verifies that get Category DTO By Id throws Category Not Found Exception.
     */
    @Test
    void testGetCategoryDtoById_throwsCategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> {

            when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

            categoryService.getCategoryDtoById(CategoryConstants.ID);

        });
    }

    /**
     * Verifies that get Category DTO By Name no Exceptions success.
     */
    @Test
    void testGetCategoryDtoByName_noExceptions_success() {
        final CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category()));

        final CategoryDto categoryDto = categoryService.getCategoryDtoByName(CategoryConstants.NAME);

        assertEquals(expected, categoryDto);
    }

    /**
     * Verifies that get Category DTO By Name throws Category Not Found Exception.
     */
    @Test
    void testGetCategoryDtoByName_throwsCategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> {

            when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

            categoryService.getCategoryDtoByName(CategoryConstants.NAME);

        });
    }

    /**
     * Verifies that update Category success.
     */
    @Test
    void testUpdateCategory_success() {
        final CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));
        when(categoryRepository.save(any())).thenReturn(CategoryFactory.getDefaultCategory());

        CategoryDto categoryDto = categoryService.updateCategory(CategoryFactory.getDefaultCategoryRequest(),
            CategoryConstants.ID);

        assertEquals(expected, categoryDto);
    }

    /**
     * Verifies that update Category category Exists throws.
     */
    @Test
    void testUpdateCategory_categoryExists_throws() {
        assertThrows(CategoryAlreadyExistsException.class, () -> {

            when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category()));

            categoryService.addCategory(CategoryFactory.getDefaultCategoryRequest());

        });
    }

    /**
     * Verifies that delete Category success.
     */
    @Test
    void testDeleteCategory_success() {
        final CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));

        final CategoryDto categoryDto = categoryService.deleteCategory(CategoryConstants.ID);

        assertEquals(expected, categoryDto);
    }

    /**
     * Verifies that get Category By Id category Not Found throws Category Not Found Exception.
     */
    @Test
    void testGetCategoryById_categoryNotFound_throwsCategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> {

            categoryService.getCategoryById(CategoryConstants.ID);

        });
    }
}



