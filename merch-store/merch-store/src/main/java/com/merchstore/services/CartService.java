package com.merchstore.services;

import com.merchstore.dtos.CartItemCreateDto;
import com.merchstore.dtos.CartOutDto;
import com.merchstore.models.CartResult;
import com.merchstore.models.User;

public interface CartService {

    CartResult getOrCreateCart(
            User currentUser,
            String cartToken
    );

    CartOutDto addItem(
            User currentUser,
            String cartToken,
            CartItemCreateDto cartItemCreateDto
    );

    CartOutDto updateQuantity(
            User currentUser,
            String cartToken,
            Long itemId,
            Integer quantity
    );

    CartOutDto deleteItem(
            User currentUser,
            String cartToken,
            Long itemId
    );

    CartOutDto clearCart(
            User currentUser,
            String cartToken
    );
}