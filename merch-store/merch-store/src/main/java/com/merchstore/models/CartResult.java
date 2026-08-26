package com.merchstore.models;

import com.merchstore.dtos.CartOutDto;

public class CartResult {

    private final CartOutDto cart;
    private final String cartToken;
    private final boolean newGuestCart;

    public CartResult(
            CartOutDto cart,
            String cartToken,
            boolean newGuestCart) {

        this.cart = cart;
        this.cartToken = cartToken;
        this.newGuestCart = newGuestCart;
    }

    public CartOutDto getCart() {
        return cart;
    }

    public String getCartToken() {
        return cartToken;
    }

    public boolean isNewGuestCart() {
        return newGuestCart;
    }
}