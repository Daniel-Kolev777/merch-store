package com.merchstore.dtos.product;

import java.math.BigDecimal;
import java.util.List;

public class ProductOutDto {

    private String name;

    private String description;

    private BigDecimal price;

    private String categoryName;

    private List<ProductImageOutDto> images;

    public ProductOutDto(
            String name,
            String description,
            BigDecimal price,
            String categoryName,
            List<ProductImageOutDto> images) {

        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryName = categoryName;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ProductImageOutDto> getImages() {
        return images;
    }

    public void setImages(List<ProductImageOutDto> images) {
        this.images = images;
    }
}