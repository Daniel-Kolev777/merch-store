package com.merchstore.dtos;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public class ProductUpdateDto {

    private String name;

    private String description;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Price must be greater than 0"
    )
    private BigDecimal price;

    private Long categoryId;

    public ProductUpdateDto() {
    }

    public ProductUpdateDto(
            String name,
            String description,
            BigDecimal price,
            Long categoryId) {

        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}