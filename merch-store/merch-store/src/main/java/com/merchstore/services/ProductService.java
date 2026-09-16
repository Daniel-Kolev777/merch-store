package com.merchstore.services;

import com.merchstore.dtos.product.ProductCreateDto;
import com.merchstore.dtos.product.ProductOutDto;
import com.merchstore.dtos.product.ProductUpdateDto;
import com.merchstore.models.User;
import com.merchstore.models.filters.ProductFilterOptions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<ProductOutDto> getAll(
            ProductFilterOptions productFilterOptions
    );

    List<ProductOutDto> getAllAdmin(
            ProductFilterOptions productFilterOptions,
            User currentUser
    );

    ProductOutDto create(
            ProductCreateDto productCreateDto,
            List<MultipartFile> images
    );

    ProductOutDto getById(
            Long productId
    );

    ProductOutDto getByIdAdmin(
            Long productId,
            User currentUser
    );

    ProductOutDto update(
            Long productId,
            ProductUpdateDto productUpdateDto,
            User executingUser
    );

    ProductOutDto changeActive(
            Long productId,
            boolean active,
            User currentUser
    );

    void delete(
            Long productId,
            User currentUser
    );
}