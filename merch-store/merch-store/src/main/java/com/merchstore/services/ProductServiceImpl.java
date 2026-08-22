package com.merchstore.services;

import com.merchstore.dtos.ProductCreateDto;
import com.merchstore.dtos.ProductOutDto;
import com.merchstore.dtos.ProductUpdateDto;
import com.merchstore.exceptions.AuthorizationException;
import com.merchstore.exceptions.BadRequestException;
import com.merchstore.exceptions.EntityNotFoundException;
import com.merchstore.exceptions.InvalidProductException;
import com.merchstore.helpers.ProductMapper;
import com.merchstore.helpers.ProductSpecificationBuilder;
import com.merchstore.helpers.RoleValidator;
import com.merchstore.models.Category;
import com.merchstore.models.Product;
import com.merchstore.models.ProductImage;
import com.merchstore.models.User;
import com.merchstore.models.filters.ProductFilterOptions;
import com.merchstore.repositories.CategoryRepository;
import com.merchstore.repositories.ProductRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ProductImageService productImageService;

    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductMapper productMapper,
            CategoryRepository categoryRepository,
            ProductImageService productImageService) {

        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.productImageService = productImageService;
    }

    @Override
    public List<ProductOutDto> getAll(
            ProductFilterOptions productFilterOptions) {

        Specification<Product> specification =
                ProductSpecificationBuilder.buildFrom(
                        productFilterOptions
                );

        List<Product> products =
                productRepository.findAll(specification);

        return productMapper.fromEntitiesToOutDto(products);
    }

    @Override
    public ProductOutDto create(
            ProductCreateDto productCreateDto,
            List<MultipartFile> images) {

        if (images == null || images.size() < 3) {
            throw new InvalidProductException(
                    "Product must have at least 3 images"
            );
        }

        for (MultipartFile image : images) {

            if (image.isEmpty()) {
                throw new InvalidProductException(
                        "Image cannot be empty"
                );
            }
        }

        Category category =
                categoryRepository.findById(
                        productCreateDto.getCategoryId()
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Category",
                                "id",
                                String.valueOf(
                                        productCreateDto.getCategoryId()
                                )
                        )
                );

        Product product = new Product();

        product.setName(productCreateDto.getName());
        product.setDescription(productCreateDto.getDescription());
        product.setPrice(productCreateDto.getPrice());
        product.setCategory(category);

        productRepository.save(product);

        for (MultipartFile image : images) {

            productImageService.uploadImage(
                    product.getId(),
                    image
            );
        }

        Product savedProduct =
                productRepository.findByIdWithImages(
                        product.getId()
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Product",
                                "id",
                                String.valueOf(
                                        product.getId()
                                )
                        )
                );

        return productMapper.fromProductToOutDto(
                savedProduct
        );
    }

    @Override
    public ProductOutDto getById(Long productId) {

        Product product =
                productRepository.findByIdWithImages(
                        productId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Product",
                                "id",
                                String.valueOf(productId)
                        )
                );

        return productMapper.fromProductToOutDto(product);
    }

    @Override
    public ProductOutDto update(
            Long productId,
            ProductUpdateDto productUpdateDto,
            User executingUser) {

        if (!RoleValidator.isAdmin(executingUser)) {
            throw new AuthorizationException(
                    "Only Admin can update products!"
            );
        }

        Product product =
                productRepository.findByIdWithImages(
                        productId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Product",
                                "id",
                                String.valueOf(productId)
                        )
                );

        if (productUpdateDto.getName() != null) {
            product.setName(
                    productUpdateDto.getName()
            );
        }

        if (productUpdateDto.getDescription() != null) {
            product.setDescription(
                    productUpdateDto.getDescription()
            );
        }

        if (productUpdateDto.getPrice() != null) {
            product.setPrice(
                    productUpdateDto.getPrice()
            );
        }

        if (productUpdateDto.getCategoryId() != null) {

            Category category =
                    categoryRepository.findById(
                            productUpdateDto.getCategoryId()
                    ).orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Category",
                                    "id",
                                    String.valueOf(
                                            productUpdateDto.getCategoryId()
                                    )
                            )
                    );

            product.setCategory(category);
        }

        Product updatedProduct =
                productRepository.save(product);

        return productMapper.fromProductToOutDto(
                updatedProduct
        );
    }

    @Override
    public void delete(
            Long productId,
            User currentUser) {

        if (!RoleValidator.isAdmin(currentUser)) {
            throw new AuthorizationException(
                    "Only Admin can delete products!"
            );
        }

        Product product =
                productRepository.findByIdWithImages(
                        productId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Product",
                                "id",
                                String.valueOf(productId)
                        )
                );

        List<ProductImage> images =
                product.getImages();

        for (ProductImage image : images) {

            Path filePath = Paths.get(
                    image.getImageURL().substring(1)
            );

            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new BadRequestException(
                        "Could not delete product image file"
                );
            }
        }

        productRepository.delete(product);
    }
}