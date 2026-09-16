package com.merchstore.dtos.product;

import com.merchstore.models.enums.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

public class ProductCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Price must be greater than 0"
    )
    private BigDecimal price;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Weight is required!")
    @DecimalMin(
            value = "0.001",
            message = "Weight must be greater than 0!"
    )
    private BigDecimal weight;

    @NotNull(message = "Has sizes is required")
    private Boolean hasSizes;

    @NotNull(message = "Quantity is required")
    @Min(
            value = 0,
            message = "Quantity cannot be negative"
    )
    private Integer quantity;

    private Set<Size> availableSizes;

    public ProductCreateDto() {
    }

    public ProductCreateDto(
            String name,
            String description,
            BigDecimal price,
            Long categoryId,
            BigDecimal weight,
            Boolean hasSizes,
            Integer quantity,
            Set<Size> availableSizes
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.weight = weight;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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