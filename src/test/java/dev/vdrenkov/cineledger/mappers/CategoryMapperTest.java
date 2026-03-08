package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.testutils.constants.CategoryConstants;
import dev.vdrenkov.cineledger.testutils.factories.CategoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests category mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class CategoryMapperTest {
    /**
     * Verifies that map Category To Category DTO List success.
     */
    @Test
    void testMapCategoryToCategoryDtoList_success() {
        List<CategoryDto> categoryDtos = CategoryMapper.mapCategoryToCategoryDtoList(
            CategoryFactory.getDefaultCategoryList());

        final CategoryDto categoryDto = categoryDtos.getFirst();
        assertEquals(CategoryConstants.ID, categoryDto.getId());
        assertEquals(CategoryConstants.NAME, categoryDto.getName());
    }

    /**
     * Verifies that map Category To Category DTO success.
     */
    @Test
    void testMapCategoryToCategoryDto_success() {
        final CategoryDto actualDto = CategoryMapper.mapCategoryToCategoryDto(CategoryFactory.getDefaultCategory());

        assertEquals(CategoryConstants.ID, actualDto.getId());
        assertEquals(CategoryConstants.NAME, actualDto.getName());
    }
}



