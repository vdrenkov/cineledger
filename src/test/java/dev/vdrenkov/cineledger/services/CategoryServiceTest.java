package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.CategoryAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CategoryNotFoundException;
import dev.vdrenkov.cineledger.mappers.CategoryMapper;
import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import dev.vdrenkov.cineledger.repositories.CategoryRepository;
import dev.vdrenkov.cineledger.testUtils.constants.CategoryConstants;
import dev.vdrenkov.cineledger.testUtils.factories.CategoryFactory;
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
public class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testAddCategory_noExceptions_success() {
        Category expected = CategoryFactory.getDefaultCategory();
        when(categoryRepository.save(any())).thenReturn(expected);

        Category category = categoryService.addCategory(CategoryFactory.getDefaultCategoryRequest());

        assertEquals(expected, category);
    }

    @Test
    public void testGetAllCategories_noExceptions_success() {
        List<CategoryDto> expected = CategoryFactory.getDefaultCategoryDtoList();

        when(categoryMapper.mapCategoryToCategoryDtoList(any())).thenReturn(expected);
        when(categoryRepository.findAll()).thenReturn(CategoryFactory.getDefaultCategoryList());

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(expected, result);
    }

    @Test
    public void testGetCategoryDtoById_noExceptions_success() {
        CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));

        CategoryDto categoryDto = categoryService.getCategoryDtoById(CategoryConstants.ID);

        assertEquals(expected, categoryDto);
    }

    @Test
    public void testGetCategoryDtoById_throwsCategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> {

            when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

            categoryService.getCategoryDtoById(CategoryConstants.ID);

        });
    }

    @Test
    public void testGetCategoryDtoByName_noExceptions_success() {
        CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category()));

        CategoryDto categoryDto = categoryService.getCategoryDtoByName(CategoryConstants.NAME);

        assertEquals(expected, categoryDto);
    }

    @Test
    public void testGetCategoryDtoByName_throwsCategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> {

            when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

            categoryService.getCategoryDtoByName(CategoryConstants.NAME);

        });
    }

    @Test
    public void testUpdateCategory_success() {
        CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));
        when(categoryRepository.save(any())).thenReturn(CategoryFactory.getDefaultCategory());

        CategoryDto categoryDto = categoryService.updateCategory(CategoryFactory.getDefaultCategoryRequest(),
            CategoryConstants.ID);

        assertEquals(expected, categoryDto);
    }

    @Test
    public void testUpdateCategory_categoryExists_throws() {
        assertThrows(CategoryAlreadyExistsException.class, () -> {

            when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category()));

            categoryService.addCategory(CategoryFactory.getDefaultCategoryRequest());

        });
    }

    @Test
    public void testDeleteCategory_success() {
        CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));

        CategoryDto categoryDto = categoryService.deleteCategory(CategoryConstants.ID);

        assertEquals(expected, categoryDto);
    }

    @Test
    public void testGetCategoryById_categoryNotFound_throwsCategoryNotFoundException() {
        assertThrows(CategoryNotFoundException.class, () -> {

            categoryService.getCategoryById(CategoryConstants.ID);

        });
    }
}



