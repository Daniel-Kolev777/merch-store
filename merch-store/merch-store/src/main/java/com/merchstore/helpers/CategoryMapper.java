package com.merchstore.helpers;

import com.merchstore.dtos.category.CategoryCreateDto;
import com.merchstore.dtos.category.CategoryOutDto;
import com.merchstore.models.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    public Category fromCategoryCreateDtoToCategory(
            CategoryCreateDto categoryCreateDto
    ) {

        Category category = new Category();

        category.setName(
                categoryCreateDto.getName()
        );

        return category;
    }

    public CategoryOutDto fromCategoryToOutDto(
            Category category
    ) {

        return new CategoryOutDto(
                category.getId(),
                category.getName()
        );
    }

    public List<CategoryOutDto> fromEntitiesToOutDto(
            List<Category> categories
    ) {

        return categories.stream()
                .map(this::fromCategoryToOutDto)
                .toList();
    }
}