package dev.vdrenkov.cineledger.testUtils.factories;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import dev.vdrenkov.cineledger.models.requests.CategoryRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testUtils.constants.CategoryConstants.ID;
import static dev.vdrenkov.cineledger.testUtils.constants.CategoryConstants.NAME;

public final class CategoryFactory {

    private CategoryFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static CategoryRequest getDefaultCategoryRequest() {
        return new CategoryRequest(NAME);
    }

    public static Category getDefaultCategory() {
        return new Category(ID, NAME);
    }

    public static CategoryDto getDefaultCategoryDto() {
        return new CategoryDto(ID, NAME);
    }

    public static List<CategoryDto> getDefaultCategoryDtoList() {
        return Collections.singletonList(getDefaultCategoryDto());
    }

    public static List<Category> getDefaultCategoryList() {
        return Collections.singletonList(getDefaultCategory());
    }
}


