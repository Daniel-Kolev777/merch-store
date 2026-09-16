package com.merchstore.helpers;

import com.merchstore.dtos.product.ProductImageOutDto;
import com.merchstore.dtos.product.ProductOutDto;
import com.merchstore.models.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final ProductImageMapper productImageMapper;

    public ProductMapper(
            ProductImageMapper productImageMapper) {

        this.productImageMapper =
                productImageMapper;
    }

    public List<ProductOutDto> fromEntitiesToOutDto(
            List<Product> products) {

        return products.stream()
                .map(this::fromProductToOutDto)
                .collect(Collectors.toList());
    }

    public ProductOutDto fromProductToOutDto(
            Product product) {

        List<ProductImageOutDto> images =
                product.getImages()
                        .stream()
                        .map(
                                productImageMapper
                                        ::fromProductImageToOutDto
                        )
                        .collect(Collectors.toList());

        return new ProductOutDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getWeight(),
                product.getCategory().getName(),
                product.isHasSizes(),
                product.getQuantity(),
                product.isActive(),
                product.getAvailableSizes(),
                images
        );
    }
}