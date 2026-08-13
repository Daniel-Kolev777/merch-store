package com.merchstore.services;

import com.merchstore.dtos.CategoryCreateDto;
import com.merchstore.dtos.CategoryOutDto;
import com.merchstore.dtos.CategoryUpdateDto;
import com.merchstore.exceptions.EntityDuplicateException;
import com.merchstore.exceptions.EntityNotFoundException;
import com.merchstore.helpers.CategoryMapper;
import com.merchstore.helpers.RoleValidator;
import com.merchstore.models.Category;
import com.merchstore.models.User;
import com.merchstore.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    public static final String ONLY_ADMIN_CAN_CREATE_CATEGORIES_ERROR_MESSAGE = "Only Admin can create categories!";
    public static final String ONLY_ADMIN_CAN_DELETE_CATEGORIES_ERROR_MESSAGE = "Only Admin can delete categories!";
    public static final String ONLY_ADMIN_CAN_UPDATE_CATEGORIES_ERROR_MESSAGE = "Only Admin can update categories!";


    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryOutDto> getAll() {
        return categoryMapper.fromEntitiesToOutDto(categoryRepository.findAllByDeletedFalse());

    }

    @Override
    public CategoryOutDto getById(Long id) {
        Category searchedCategory = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category", id)
                );
        return categoryMapper.fromCategoryToOutDto(searchedCategory);
    }

    @Override
    public CategoryOutDto create(User executingUser, CategoryCreateDto categoryCreateDto) {

        if (!RoleValidator.isAdmin(executingUser)) {
            throw new IllegalArgumentException(
                    ONLY_ADMIN_CAN_CREATE_CATEGORIES_ERROR_MESSAGE
            );
        }

        Optional<Category> existingCategory =
                categoryRepository.findByName(categoryCreateDto.getName());

        if (existingCategory.isPresent()) {

            Category category = existingCategory.get();

            if (category.isDeleted()) {
                throw new EntityDuplicateException(
                        "Category with this name already exists go and restore it."
                );
            }

            throw new EntityDuplicateException(
                    "Category",
                    "name",
                    categoryCreateDto.getName()
            );
        }

        Category newCategory =
                categoryMapper.fromCategoryCreateDtoToCategory(categoryCreateDto);

        Category savedCategory = categoryRepository.save(newCategory);

        return categoryMapper.fromCategoryToOutDto(savedCategory);
    }


    @Override
    public CategoryOutDto update(Long id, User executingUser, CategoryUpdateDto categoryUpdateDto) {
        Category categoryToUpdate = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category", id)
                );

        if (RoleValidator.isAdmin(executingUser)){
            List<Category> categories = categoryRepository.findAllByDeletedFalse();

            for (Category category : categories) {

                if (category.getName().equalsIgnoreCase(categoryUpdateDto.getName())) {

                    throw new EntityDuplicateException(
                            "Category",
                            "name",
                            categoryUpdateDto.getName()
                    );
                }
            }
            categoryToUpdate.setName(categoryUpdateDto.getName());
            categoryRepository.save(categoryToUpdate);
        }else {
            throw new IllegalArgumentException(ONLY_ADMIN_CAN_UPDATE_CATEGORIES_ERROR_MESSAGE);
        }

        return categoryMapper.fromCategoryToOutDto(categoryToUpdate);
    }

    @Override
    public CategoryOutDto delete(Long id, User executingUser) {

        if (!RoleValidator.isAdmin(executingUser)) {
            throw new IllegalArgumentException(
                    ONLY_ADMIN_CAN_DELETE_CATEGORIES_ERROR_MESSAGE
            );
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category", id)
                );

        category.setDeleted(true);

        Category deletedCategory = categoryRepository.save(category);

        return categoryMapper.fromCategoryToOutDto(deletedCategory);
    }

    @Override
    public CategoryOutDto restore(Long id, User executingUser) {

        if (!RoleValidator.isAdmin(executingUser)) {
            throw new IllegalArgumentException(
                    ONLY_ADMIN_CAN_DELETE_CATEGORIES_ERROR_MESSAGE
            );
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category", id)
                );

        category.setDeleted(false);

        Category deletedCategory = categoryRepository.save(category);

        return categoryMapper.fromCategoryToOutDto(deletedCategory);
    }
}
