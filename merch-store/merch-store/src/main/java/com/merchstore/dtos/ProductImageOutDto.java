package com.merchstore.dtos;

public class ProductImageOutDto {

    private String imageUrl;

    public ProductImageOutDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
