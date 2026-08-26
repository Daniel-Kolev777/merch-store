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

    ProductOutDto create(
            ProductCreateDto productCreateDto,
            List<MultipartFile> images
    );

    ProductOutDto getById(Long productId);

    ProductOutDto update(
            Long productId,
            ProductUpdateDto productUpdateDto,
            User executingUser
    );

    void delete(
            Long productId,
            User currentUser
    );
}