package com.merchstore.helpers;

import com.merchstore.dtos.CartItemOutDto;
import com.merchstore.dtos.CartOutDto;
import com.merchstore.models.Cart;
import com.merchstore.models.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartOutDto fromCartToOutDto(Cart cart) {

        List<CartItemOutDto> items =
                cart.getItems()
                        .stream()
                        .map(this::fromCartItemToOutDto)
                        .toList();

        return new CartOutDto(
                items
        );
    }

    private CartItemOutDto fromCartItemToOutDto(
            CartItem cartItem) {

        return new CartItemOutDto(
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity(),
                cartItem.getSize()
        );
    }
}