package com.merchstore.dtos.order;

import com.merchstore.models.enums.Size;

import java.math.BigDecimal;

public class OrderItemOutDto {

    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private Size size;

    public OrderItemOutDto() {
    }

    public OrderItemOutDto(
            String productName,
            BigDecimal price,
            Integer quantity,
            Size size) {

        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
