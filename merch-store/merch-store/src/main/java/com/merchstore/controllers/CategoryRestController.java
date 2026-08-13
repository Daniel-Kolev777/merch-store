package com.merchstore.controllers;

import com.merchstore.dtos.CategoryCreateDto;
import com.merchstore.dtos.CategoryOutDto;
import com.merchstore.dtos.CategoryUpdateDto;
import com.merchstore.models.User;
import com.merchstore.services.CategoryService;
import com.merchstore.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private CategoryService categoryService;
    private UserService userService;

    public CategoryRestController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    //    createCategory(CreateDto dto)
//    getAllCategories()
//    getCategoryById(Long id)
//    updateCategory(Long id, UpdateDto dto)
//    deleteCategory(Long id)

    @GetMapping
    public List<CategoryOutDto> getAll() {
        return categoryService.getAll();

    }

    @GetMapping("/{id}")
    public CategoryOutDto getById(@PathVariable Long id){

        return categoryService.getById(id);
    }

    @PostMapping
    public CategoryOutDto create(@RequestBody CategoryCreateDto categoryCreateDto, Authentication authentication){
        User currentUser = userService.getUserByUsername(authentication.getName());

        return categoryService.create(currentUser, categoryCreateDto);
    }

    @PutMapping("/{id}")
    private CategoryOutDto update(@PathVariable Long id,
                                  @RequestBody CategoryUpdateDto categoryUpdateDto,
                                  Authentication authentication){
        User currentUser = userService.getUserByUsername(authentication.getName());

        return categoryService.update(id, currentUser, categoryUpdateDto);
    }

    @DeleteMapping("/{id}")
    public CategoryOutDto delete(
            @PathVariable Long id,
            Authentication authentication) {

        User currentUser = userService.getUserByUsername(authentication.getName());

        return categoryService.delete(id, currentUser);
    }

    @PutMapping("/{id}/restore")
    private CategoryOutDto restore(@PathVariable Long id,
                                   Authentication authentication){
        User currentUser = userService.getUserByUsername(authentication.getName());

        return categoryService.restore(id, currentUser);
    }

}
