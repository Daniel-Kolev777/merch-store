package com.merchstore.services;

import com.merchstore.dtos.category.CategoryCreateDto;
import com.merchstore.dtos.category.CategoryOutDto;
import com.merchstore.dtos.category.CategoryUpdateDto;
import com.merchstore.models.User;

import java.util.List;

public interface CategoryService {

    List<CategoryOutDto> getAll();

    CategoryOutDto getById(Long id);

    CategoryOutDto create(User executingUser, CategoryCreateDto categoryCreateDto);

    CategoryOutDto update(Long id, User executingUser, CategoryUpdateDto categoryUpdateDto);

    CategoryOutDto delete(Long id, User executingUser);

    CategoryOutDto restore(Long id, User executingUser);
}
