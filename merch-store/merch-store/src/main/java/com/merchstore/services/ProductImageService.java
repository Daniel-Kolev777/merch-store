package com.merchstore.services;

import com.merchstore.dtos.product.ProductImageOutDto;
import com.merchstore.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductImageService {

    ProductImageOutDto uploadImage(Long id, MultipartFile file);

    List<ProductImageOutDto> getImagesByProductId(Long productId);

    void deleteImage(Long imageId, User currentUser) throws IOException;

    ProductImageOutDto getImageById(Long imageId);

    ProductImageOutDto updateImage(
            Long imageId,
            MultipartFile file,
            User currentUser
    ) throws IOException;
}
