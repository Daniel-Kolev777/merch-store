package com.merchstore.dtos;

import java.math.BigDecimal;

public class CartItemOutDto {

    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;

    public CartItemOutDto() {
    }

    public CartItemOutDto(
            String productName,
            BigDecimal productPrice,
            Integer quantity) {

        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
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

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
