package com.merchstore.dtos;

import com.merchstore.models.enums.Size;

import java.math.BigDecimal;

public class CartItemOutDto {

    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private Size size;

    public CartItemOutDto() {
    }

    public CartItemOutDto(
            String productName,
            BigDecimal productPrice,
            Integer quantity,
            Size size) {

        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.size = size;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Size getSize() {
        return size;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}