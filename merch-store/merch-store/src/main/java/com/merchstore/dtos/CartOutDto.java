package com.merchstore.dtos;

import java.util.List;

public class CartOutDto {

    private List<CartItemOutDto> items;

    public CartOutDto() {
    }

    public CartOutDto(List<CartItemOutDto> items) {
        this.items = items;
    }

    public List<CartItemOutDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemOutDto> items) {
        this.items = items;
    }
}
