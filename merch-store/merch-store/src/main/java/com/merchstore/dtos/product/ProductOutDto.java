package com.merchstore.dtos.product;

import com.merchstore.models.enums.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class ProductOutDto {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal weight;

    private String category;

    private boolean hasSizes;

    private Integer quantity;

    private boolean active;

    private Set<Size> availableSizes;

    private List<ProductImageOutDto> images;

    public ProductOutDto() {
    }

    public ProductOutDto(
            Long id,
            String name,
            String description,
            BigDecimal price,
            BigDecimal weight,
            String category,
            boolean hasSizes,
            Integer quantity,
            boolean active,
            Set<Size> availableSizes,
            List<ProductImageOutDto> images
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.category = category;
        this.hasSizes = hasSizes;
        this.quantity = quantity;
        this.active = active;
        this.availableSizes = availableSizes;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isHasSizes() {
        return hasSizes;
    }

    public void setHasSizes(boolean hasSizes) {
        this.hasSizes = hasSizes;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Size> getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(Set<Size> availableSizes) {
        this.availableSizes = availableSizes;
    }

    public List<ProductImageOutDto> getImages() {
        return images;
    }

    public void setImages(List<ProductImageOutDto> images) {
        this.images = images;
    }
}