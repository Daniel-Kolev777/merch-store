package com.merchstore.controllers;

import com.merchstore.dtos.product.ProductImageOutDto;
import com.merchstore.models.User;
import com.merchstore.services.ProductImageService;
import com.merchstore.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/product_images")
public class ProductImageRestController {

    public static final String IMAGE_DELETED_SUCCESSFULLY_MESSAGE = "Image deleted successfully!";
    private ProductImageService productImageService;
    private UserService userService;

    public ProductImageRestController(ProductImageService productImageService, UserService userService) {
        this.productImageService = productImageService;
        this.userService = userService;
    }

    @PostMapping("/product/{productId}/images")
    public ProductImageOutDto uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {

        return productImageService.uploadImage(productId, file);
    }

    @GetMapping("/product/{productId}/images")
    public List<ProductImageOutDto> getImagesByProductId(
            @PathVariable Long productId) {

        return productImageService.getImagesByProductId(productId);
    }

    @DeleteMapping("/{imageId}")
    public String deleteImage(@PathVariable Long imageId,
                              Authentication authentication) throws IOException {

        User currentUser = userService.getUserByUsername(authentication.getName());

        productImageService.deleteImage(imageId, currentUser);

        return IMAGE_DELETED_SUCCESSFULLY_MESSAGE;
    }

    @GetMapping("/{imageId}")
    public ProductImageOutDto getImageById(
            @PathVariable Long imageId) {

        return productImageService.getImageById(imageId);
    }

    @PutMapping("/{imageId}")
    public ProductImageOutDto updateImage(
            @PathVariable Long imageId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        User currentUser =
                userService.getUserByUsername(authentication.getName());

        return productImageService.updateImage(
                imageId,
                file,
                currentUser
        );
    }
}
