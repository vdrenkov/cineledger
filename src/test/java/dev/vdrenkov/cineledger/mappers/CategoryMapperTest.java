package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.testutils.constants.CategoryConstants;
import dev.vdrenkov.cineledger.testutils.factories.CategoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CategoryMapperTest {

    @Test
    void testMapCategoryToCategoryDtoList_success() {
        List<CategoryDto> categoryDtos = CategoryMapper.mapCategoryToCategoryDtoList(
            CategoryFactory.getDefaultCategoryList());

        final CategoryDto categoryDto = categoryDtos.getFirst();
        assertEquals(CategoryConstants.ID, categoryDto.id());
        assertEquals(CategoryConstants.NAME, categoryDto.name());
    }

    @Test
    void testMapCategoryToCategoryDto_success() {
        final CategoryDto actualDto = CategoryMapper.mapCategoryToCategoryDto(CategoryFactory.getDefaultCategory());

        assertEquals(CategoryConstants.ID, actualDto.id());
        assertEquals(CategoryConstants.NAME, actualDto.name());
    }
}
