package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.CategoryAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CategoryNotFoundException;
import dev.vdrenkov.cineledger.mappers.CategoryMapper;
import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import dev.vdrenkov.cineledger.models.requests.CategoryRequest;
import dev.vdrenkov.cineledger.repositories.CategoryRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(CategoryRequest categoryRequest) {

        categoryValidation(categoryRequest);

        return categoryRepository.save(new Category(categoryRequest.name()));
    }

    public List<CategoryDto> getAllCategories() {

        return CategoryMapper.mapCategoryToCategoryDtoList(categoryRepository.findAll());
    }

    public CategoryDto getCategoryDtoById(int id) {

        return CategoryMapper.mapCategoryToCategoryDto(getCategoryById(id));
    }

    public CategoryDto getCategoryDtoByName(String name) {

        return CategoryMapper.mapCategoryToCategoryDto(categoryRepository.findByName(name).orElseThrow(() -> {

            return new CategoryNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE);
        }));
    }

    public Category getCategoryById(int id) {

        return categoryRepository.findById(id).orElseThrow(() -> {

            return new CategoryNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE);
        });
    }

    public CategoryDto updateCategory(CategoryRequest categoryRequest, int categoryId) {
        final CategoryDto categoryDto = getCategoryDtoById(categoryId);

        categoryRepository.save(new Category(categoryId, categoryRequest.name()));
        return categoryDto;
    }

    public CategoryDto deleteCategory(int categoryId) {
        final CategoryDto categoryDto = getCategoryDtoById(categoryId);
        categoryRepository.deleteById(categoryId);

        return categoryDto;
    }

    private void categoryValidation(CategoryRequest categoryRequest) {
        categoryRepository.findByName(categoryRequest.name()).ifPresent(category -> {

            throw new CategoryAlreadyExistsException(ExceptionMessages.CATEGORY_ALREADY_EXISTS_MESSAGE);
        });
    }
}
