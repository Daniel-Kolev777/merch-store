package com.merchstore.dtos;

public class CartItemCreateDto {

    private Long productId;
    private Integer quantity;
    private String size;

    public CartItemCreateDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
