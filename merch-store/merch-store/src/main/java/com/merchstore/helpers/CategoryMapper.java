package com.merchstore.helpers;

import com.merchstore.dtos.CategoryCreateDto;
import com.merchstore.dtos.CategoryOutDto;
import com.merchstore.dtos.CategoryUpdateDto;
import com.merchstore.models.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public List<CategoryOutDto> fromEntitiesToOutDto(List<Category> categories) {
        return categories.stream()
                .map(this::fromCategoryToOutDto)
                .collect(Collectors.toList());
    }

    public CategoryOutDto fromCategoryToOutDto(Category category){
        return new CategoryOutDto(
                category.getName(),
                category.getProducts());
    }

    public Category fromCategoryCreateDtoToCategory(CategoryCreateDto categoryCreateDto){
        return new Category(categoryCreateDto.getName());
    }

    public Category fromCategoryUpdateDtoToCategory(CategoryUpdateDto categoryUpdateDto){
        return new Category(categoryUpdateDto.getName());
    }
}
