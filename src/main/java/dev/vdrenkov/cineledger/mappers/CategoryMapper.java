package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps category domain models to DTO representations used by the API.
 */
@Component
public class CategoryMapper {
    private static final Logger log = LoggerFactory.getLogger(CategoryMapper.class);

    /**
     * Maps category values to category dto values.
     *
     * @param category
     *     category entity to transform
     * @return category dto result
     */
    public CategoryDto mapCategoryToCategoryDto(Category category) {
        log.info(String.format("The category %s is being mapped to a category DTO", category.getName()));
        return new CategoryDto(category.getId(), category.getName());
    }

    /**
     * Maps category values to category dto list values.
     *
     * @param categories
     *     category entities to transform
     * @return matching category dto values
     */
    public List<CategoryDto> mapCategoryToCategoryDtoList(List<Category> categories) {
        return categories.stream().map(this::mapCategoryToCategoryDto).collect(Collectors.toList());
    }
}

