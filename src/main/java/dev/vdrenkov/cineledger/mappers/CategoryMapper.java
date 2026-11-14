package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;

import java.util.List;

public final class CategoryMapper {

    private CategoryMapper() {

    }

    public static CategoryDto mapCategoryToCategoryDto(Category category) {

        return new CategoryDto(category.getId(), category.getName());
    }

    public static List<CategoryDto> mapCategoryToCategoryDtoList(List<Category> categories) {
        return categories.stream().map(CategoryMapper::mapCategoryToCategoryDto).toList();
    }
}
