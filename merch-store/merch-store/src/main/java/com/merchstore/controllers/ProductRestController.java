package com.merchstore.controllers;

import com.merchstore.dtos.product.ProductCreateDto;
import com.merchstore.dtos.product.ProductOutDto;
import com.merchstore.dtos.product.ProductUpdateDto;
import com.merchstore.models.User;
import com.merchstore.models.filters.ProductFilterOptions;
import com.merchstore.services.ProductService;
import com.merchstore.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private ProductService productService;
    private UserService userService;

    public ProductRestController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public List<ProductOutDto> getAll(
            @ModelAttribute ProductFilterOptions productFilterOptions) {

        return productService.getAll(productFilterOptions);
    }

    @GetMapping("/{id}")
    private ProductOutDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductOutDto create(
            @Valid @ModelAttribute ProductCreateDto productCreateDto,
            @RequestParam("images") List<MultipartFile> images) {

        return productService.create(productCreateDto, images);
    }

    @PutMapping("/{productId}")
    public ProductOutDto update(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateDto productUpdateDto,
            Authentication authentication) {

        User currentUser = userService.getUserByUsername(authentication.getName());

        return productService.update(
                productId,
                productUpdateDto,
                currentUser
        );
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(
            @PathVariable Long productId,
            Authentication authentication) {

        User currentUser =
                userService.getUserByUsername(
                        authentication.getName()
                );

        productService.delete(
                productId,
                currentUser
        );

        return "Product deleted successfully!";
    }
}
