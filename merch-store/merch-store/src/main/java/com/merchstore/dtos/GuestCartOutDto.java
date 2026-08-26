package com.merchstore.dtos;

public class GuestCartOutDto {

    private String cartToken;

    public GuestCartOutDto() {
    }

    public GuestCartOutDto(String cartToken) {
        this.cartToken = cartToken;
    }

    public String getCartToken() {
        return cartToken;
    }

    public void setCartToken(String cartToken) {
        this.cartToken = cartToken;
    }
}
