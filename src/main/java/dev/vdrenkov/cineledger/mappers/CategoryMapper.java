package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Maps category domain models to DTO representations used by the API.
 */
public final class CategoryMapper {
    private static final Logger log = LoggerFactory.getLogger(CategoryMapper.class);

    private CategoryMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps category values to category dto values.
     *
     * @param category
     *     category entity to transform
     * @return category dto result
     */
    public static CategoryDto mapCategoryToCategoryDto(Category category) {
        log.info("Mapping the category {} to a category DTO", category.getName());
        return new CategoryDto(category.getId(), category.getName());
    }

    /**
     * Maps category values to category dto list values.
     *
     * @param categories
     *     category entities to transform
     * @return matching category dto values
     */
    public static List<CategoryDto> mapCategoryToCategoryDtoList(List<Category> categories) {
        return categories.stream().map(CategoryMapper::mapCategoryToCategoryDto).toList();
    }
}

