package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import dev.vdrenkov.cineledger.models.requests.CategoryRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.CategoryConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.CategoryConstants.NAME;

/**
 * Provides reusable category fixtures for tests.
 */
public final class CategoryFactory {

    private CategoryFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default category request fixture used in tests.
     *
     * @return test category request value
     */
    public static CategoryRequest getDefaultCategoryRequest() {
        return new CategoryRequest(NAME);
    }

    /**
     * Returns the default category fixture used in tests.
     *
     * @return test category value
     */
    public static Category getDefaultCategory() {
        return new Category(ID, NAME);
    }

    /**
     * Returns the default category dto fixture used in tests.
     *
     * @return test category dto value
     */
    public static CategoryDto getDefaultCategoryDto() {
        return new CategoryDto(ID, NAME);
    }

    /**
     * Returns the default category dto list fixture used in tests.
     *
     * @return test category dto values
     */
    public static List<CategoryDto> getDefaultCategoryDtoList() {
        return Collections.singletonList(getDefaultCategoryDto());
    }

    /**
     * Returns the default category list fixture used in tests.
     *
     * @return test category values
     */
    public static List<Category> getDefaultCategoryList() {
        return Collections.singletonList(getDefaultCategory());
    }
}


