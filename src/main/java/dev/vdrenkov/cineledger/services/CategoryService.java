package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.CategoryAlreadyExistsException;
import dev.vdrenkov.cineledger.exceptions.CategoryNotFoundException;
import dev.vdrenkov.cineledger.mappers.CategoryMapper;
import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import dev.vdrenkov.cineledger.models.requests.CategoryRequest;
import dev.vdrenkov.cineledger.repositories.CategoryRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import dev.vdrenkov.cineledger.utils.constants.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains business logic for category operations.
 */
@Service
public class CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    /**
     * Creates a new category service with its required collaborators.
     *
     * @param categoryRepository
     *     category repository used by the operation
     */
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates and persists category.
     *
     * @param categoryRequest
     *     request payload for the category operation
     * @return requested category value
     */
    public Category addCategory(CategoryRequest categoryRequest) {
        log.info("An attempt to save a category in the database");

        categoryValidation(categoryRequest);

        return categoryRepository.save(new Category(categoryRequest.getName()));
    }

    /**
     * Returns all categories matching the supplied criteria.
     *
     * @return matching category dto values
     */
    public List<CategoryDto> getAllCategories() {
        log.info("An attempt to extract all categories from the database");

        return CategoryMapper.mapCategoryToCategoryDtoList(categoryRepository.findAll());
    }

    /**
     * Returns category matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return category dto result
     */
    public CategoryDto getCategoryDtoById(int id) {
        log.info("An attempt to extract category DTO with id {} from the database", id);

        return CategoryMapper.mapCategoryToCategoryDto(getCategoryById(id));
    }

    /**
     * Returns category matching the supplied criteria.
     *
     * @param name
     *     name used by the operation
     * @return category dto result
     */
    public CategoryDto getCategoryDtoByName(String name) {
        log.info("An attempt to extract category with name {} from the database", name);

        return CategoryMapper.mapCategoryToCategoryDto(categoryRepository.findByName(name).orElseThrow(() -> {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE);

            return new CategoryNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE);
        }));
    }

    /**
     * Returns category matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @return requested category value
     */
    public Category getCategoryById(int id) {
        log.info("An attempt to extract category with id {} from the database", id);

        return categoryRepository.findById(id).orElseThrow(() -> {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE);

            return new CategoryNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE);
        });
    }

    /**
     * Updates category and returns the previous state when needed.
     *
     * @param categoryRequest
     *     request payload for the category operation
     * @param categoryId
     *     identifier of the target category
     * @return category dto result
     */
    public CategoryDto updateCategory(CategoryRequest categoryRequest, int categoryId) {
        final CategoryDto categoryDto = getCategoryDtoById(categoryId);

        log.info("An attempt to update category with id {} in the database", categoryId);
        categoryRepository.save(new Category(categoryId, categoryRequest.getName()));
        return categoryDto;
    }

    /**
     * Deletes category and returns the removed state when needed.
     *
     * @param categoryId
     *     identifier of the target category
     * @return category dto result
     */
    public CategoryDto deleteCategory(int categoryId) {
        final CategoryDto categoryDto = getCategoryDtoById(categoryId);
        categoryRepository.deleteById(categoryId);

        log.info("Category with id {} was deleted", categoryId);

        return categoryDto;
    }

    private void categoryValidation(CategoryRequest categoryRequest) {
        categoryRepository.findByName(categoryRequest.getName()).ifPresent(category -> {
            log.error(LogMessages.EXCEPTION_CAUGHT_LOG, ExceptionMessages.CATEGORY_ALREADY_EXISTS_MESSAGE);

            throw new CategoryAlreadyExistsException(ExceptionMessages.CATEGORY_ALREADY_EXISTS_MESSAGE);
        });
    }
}


