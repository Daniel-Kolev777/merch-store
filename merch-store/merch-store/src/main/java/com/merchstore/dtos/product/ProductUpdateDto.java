package com.merchstore.dtos.product;

import com.merchstore.models.enums.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.Set;

public class ProductUpdateDto {

    private String name;

    private String description;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Price must be greater than 0"
    )
    private BigDecimal price;

    @DecimalMin(
            value = "0.001",
            inclusive = true,
            message = "Weight must be greater than 0"
    )
    private BigDecimal weight;

    private Long categoryId;

    private Boolean hasSizes;

    @Min(
            value = 0,
            message = "Quantity cannot be negative"
    )
    private Integer quantity;

    private Set<Size> availableSizes;

    public ProductUpdateDto() {
    }

    public ProductUpdateDto(
            String name,
            String description,
            BigDecimal price,
            BigDecimal weight,
            Long categoryId,
            Boolean hasSizes,
            Integer quantity,
            Set<Size> availableSizes
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.categoryId = categoryId;
        this.hasSizes = hasSizes;
        this.quantity = quantity;
        this.availableSizes = availableSizes;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getHasSizes() {
        return hasSizes;
    }

    public void setHasSizes(Boolean hasSizes) {
        this.hasSizes = hasSizes;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<Size> getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(Set<Size> availableSizes) {
        this.availableSizes = availableSizes;
    }
}