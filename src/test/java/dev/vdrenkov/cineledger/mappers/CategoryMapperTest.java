package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.testutil.constants.CategoryConstants;
import dev.vdrenkov.cineledger.testutil.factories.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Tests category mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class CategoryMapperTest {

    @InjectMocks
    private CategoryMapper categoryMapper;

    /**
     * Verifies that map Category To Category DTO List success.
     */
    @Test
    void testMapCategoryToCategoryDtoList_success() {
        List<CategoryDto> categoryDtos = categoryMapper.mapCategoryToCategoryDtoList(
            CategoryFactory.getDefaultCategoryList());

        final CategoryDto categoryDto = categoryDtos.get(0);
        Assertions.assertEquals(categoryDto.getId(), CategoryConstants.ID);
        Assertions.assertEquals(categoryDto.getName(), CategoryConstants.NAME);
    }

    /**
     * Verifies that map Category To Category DTO success.
     */
    @Test
    void testMapCategoryToCategoryDto_success() {
        final CategoryDto actualDto = categoryMapper.mapCategoryToCategoryDto(CategoryFactory.getDefaultCategory());

        Assertions.assertEquals(actualDto.getId(), CategoryConstants.ID);
        Assertions.assertEquals(actualDto.getName(), CategoryConstants.NAME);
    }
}



