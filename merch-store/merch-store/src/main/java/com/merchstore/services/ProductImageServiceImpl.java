package com.merchstore.services;

import com.merchstore.dtos.product.ProductImageOutDto;
import com.merchstore.exceptions.AuthorizationException;
import com.merchstore.exceptions.BadRequestException;
import com.merchstore.exceptions.EntityNotFoundException;
import com.merchstore.helpers.ProductImageMapper;
import com.merchstore.helpers.RoleValidator;
import com.merchstore.models.Product;
import com.merchstore.models.ProductImage;
import com.merchstore.models.User;
import com.merchstore.repositories.ProductImageRepository;
import com.merchstore.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;

    public ProductImageServiceImpl(
            ProductImageRepository productImageRepository,
            ProductRepository productRepository,
            ProductImageMapper productImageMapper) {

        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public ProductImageOutDto uploadImage(Long id, MultipartFile file) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Product",
                                "id",
                                String.valueOf(id)
                        ));

        String originalFilename = file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(
                    originalFilename.lastIndexOf(".")
            );
        }

        String fileName = UUID.randomUUID() + extension;

        Path uploadPath = Paths.get("uploads/products");

        try {
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            ProductImage productImage = new ProductImage(
                    "/uploads/products/" + fileName
            );

            productImage.setProduct(product);

            productImageRepository.save(productImage);

            product.getImages().add(productImage);

            return productImageMapper.fromProductImageToOutDto(productImage);

        } catch (IOException e) {
            throw new RuntimeException("Could not save image", e);
        }
    }

    @Override
    public List<ProductImageOutDto> getImagesByProductId(Long productId) {

        List<ProductImage> images =
                productImageRepository.findByProductId(productId);

        return productImageMapper.fromListOfImagesToOutDto(images);
    }

    @Override
    public void deleteImage(Long imageId, User currentUser) throws IOException {

        if (!RoleValidator.isAdmin(currentUser)) {
            throw new AuthorizationException(
                    "Only Admin can delete images!"
            );
        }

        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Image",
                                "id",
                                String.valueOf(imageId)
                        ));

        Product product = productImage.getProduct();

        List<ProductImage> images =
                productImageRepository.findByProductId(product.getId());

        if (images.size() - 1 < 3) {
            throw new BadRequestException(
                    "Product must have at least 3 images!"
            );
        }

        Path filePath = Paths.get(
                productImage.getImageURL().substring(1)
        );

        Files.deleteIfExists(filePath);

        productImageRepository.delete(productImage);
    }
    @Override
    public ProductImageOutDto getImageById(Long imageId) {

        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Image",
                                "id",
                                String.valueOf(imageId)
                        ));

        return productImageMapper.fromProductImageToOutDto(productImage);
    }

    @Override
    public ProductImageOutDto updateImage(
            Long imageId,
            MultipartFile file,
            User currentUser) throws IOException {

        if (!RoleValidator.isAdmin(currentUser)) {
            throw new AuthorizationException(
                    "Only Admin can update images!"
            );
        }

        if (file == null || file.isEmpty()) {
            throw new BadRequestException(
                    "Image cannot be empty!"
            );
        }

        ProductImage productImage =
                productImageRepository.findById(imageId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Image",
                                        "id",
                                        String.valueOf(imageId)
                                ));

        String originalFilename = file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null &&
                originalFilename.contains(".")) {

            extension = originalFilename.substring(
                    originalFilename.lastIndexOf(".")
            );
        }

        String fileName = UUID.randomUUID() + extension;

        Path uploadPath = Paths.get("uploads/products");

        Files.createDirectories(uploadPath);

        Path newFilePath = uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                newFilePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        Path oldFilePath = Paths.get(
                productImage.getImageURL().substring(1)
        );

        Files.deleteIfExists(oldFilePath);

        productImage.setImageURL(
                "/uploads/products/" + fileName
        );

        ProductImage updatedImage =
                productImageRepository.save(productImage);

        return productImageMapper.fromProductImageToOutDto(
                updatedImage
        );
    }
}