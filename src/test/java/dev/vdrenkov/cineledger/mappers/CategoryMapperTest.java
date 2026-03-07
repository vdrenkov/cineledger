package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.testUtils.constants.CategoryConstants;
import dev.vdrenkov.cineledger.testUtils.factories.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryMapperTest {

    @InjectMocks
    private CategoryMapper categoryMapper;

    @Test
    public void testMapCategoryToCategoryDtoList_success() {
        List<CategoryDto> categoryDtos = categoryMapper.mapCategoryToCategoryDtoList(
            CategoryFactory.getDefaultCategoryList());

        CategoryDto categoryDto = categoryDtos.get(0);
        Assertions.assertEquals(categoryDto.getId(), CategoryConstants.ID);
        Assertions.assertEquals(categoryDto.getName(), CategoryConstants.NAME);
    }

    @Test
    public void testMapCategoryToCategoryDto_success() {
        CategoryDto actualDto = categoryMapper.mapCategoryToCategoryDto(CategoryFactory.getDefaultCategory());

        Assertions.assertEquals(actualDto.getId(), CategoryConstants.ID);
        Assertions.assertEquals(actualDto.getName(), CategoryConstants.NAME);
    }
}



