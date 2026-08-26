package com.merchstore.helpers;

import com.merchstore.dtos.product.ProductImageOutDto;
import com.merchstore.models.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductImageMapper {

    public ProductImageOutDto fromProductImageToOutDto(ProductImage productImage) {
        return new ProductImageOutDto(productImage.getImageURL());
    }

    public List<ProductImageOutDto> fromListOfImagesToOutDto(List<ProductImage> images) {
        return images.stream()
                .map(this::fromProductImageToOutDto)
                .toList();
    }
}
