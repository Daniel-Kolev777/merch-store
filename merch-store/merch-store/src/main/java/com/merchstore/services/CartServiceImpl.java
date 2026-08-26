package com.merchstore.services;

import com.merchstore.dtos.CartItemCreateDto;
import com.merchstore.dtos.CartOutDto;
import com.merchstore.exceptions.AuthorizationException;
import com.merchstore.exceptions.BadRequestException;
import com.merchstore.exceptions.EntityNotFoundException;
import com.merchstore.helpers.CartMapper;
import com.merchstore.models.Cart;
import com.merchstore.models.CartItem;
import com.merchstore.models.CartResult;
import com.merchstore.models.Product;
import com.merchstore.models.User;
import com.merchstore.repositories.CartItemRepository;
import com.merchstore.repositories.CartRepository;
import com.merchstore.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            CartMapper cartMapper) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public CartResult getOrCreateCart(
            User currentUser,
            String cartToken) {

        boolean newGuestCart =
                currentUser == null
                        && cartToken == null;

        Cart cart =
                findOrCreateCart(
                        currentUser,
                        cartToken
                );

        String newCartToken =
                newGuestCart
                        ? cart.getCartToken()
                        : null;

        return new CartResult(
                cartMapper.fromCartToOutDto(cart),
                newCartToken,
                newGuestCart
        );
    }

    @Override
    public CartOutDto addItem(
            User currentUser,
            String cartToken,
            CartItemCreateDto cartItemCreateDto) {

        Cart cart =
                findOrCreateCart(
                        currentUser,
                        cartToken
                );

        Product product =
                productRepository.findById(
                        cartItemCreateDto.getProductId()
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Product",
                                "id",
                                String.valueOf(
                                        cartItemCreateDto.getProductId()
                                )
                        )
                );

        CartItem cartItem =
                cartItemRepository
                        .findByCartIdAndProductId(
                                cart.getId(),
                                product.getId()
                        )
                        .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity()
                            + cartItemCreateDto.getQuantity()
            );

        } else {

            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(
                    cartItemCreateDto.getQuantity()
            );

            cart.getItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);

        return cartMapper.fromCartToOutDto(cart);
    }

    @Override
    public CartOutDto updateQuantity(
            User currentUser,
            String cartToken,
            Long itemId,
            Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new BadRequestException(
                    "Quantity must be greater than 0!"
            );
        }

        Cart cart =
                findOrCreateCart(
                        currentUser,
                        cartToken
                );

        CartItem cartItem =
                cartItemRepository.findById(itemId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "CartItem",
                                        "id",
                                        String.valueOf(itemId)
                                )
                        );

        if (!cartItem.getCart().getId()
                .equals(cart.getId())) {

            throw new AuthorizationException(
                    "CartItem does not belong to this cart!"
            );
        }

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        return cartMapper.fromCartToOutDto(cart);
    }

    @Override
    public CartOutDto deleteItem(
            User currentUser,
            String cartToken,
            Long itemId) {

        Cart cart =
                findOrCreateCart(
                        currentUser,
                        cartToken
                );

        CartItem cartItem =
                cartItemRepository.findById(itemId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "CartItem",
                                        "id",
                                        String.valueOf(itemId)
                                )
                        );

        if (!cartItem.getCart().getId()
                .equals(cart.getId())) {

            throw new AuthorizationException(
                    "CartItem does not belong to this cart!"
            );
        }

        cartItemRepository.delete(cartItem);

        cart.getItems().remove(cartItem);

        return cartMapper.fromCartToOutDto(cart);
    }

    @Override
    public CartOutDto clearCart(
            User currentUser,
            String cartToken) {

        Cart cart =
                findOrCreateCart(
                        currentUser,
                        cartToken
                );

        cart.getItems().clear();

        cartRepository.save(cart);

        return cartMapper.fromCartToOutDto(cart);
    }

    private Cart findOrCreateCart(
            User currentUser,
            String cartToken) {

        if (currentUser != null) {

            return cartRepository.findByUserId(
                    currentUser.getId()
            ).orElseGet(() -> {

                Cart cart = new Cart();

                cart.setUser(currentUser);

                return cartRepository.save(cart);
            });
        }

        if (cartToken != null) {

            return cartRepository.findByCartToken(
                    cartToken
            ).orElseThrow(() ->
                    new EntityNotFoundException(
                            "Cart",
                            "cartToken",
                            cartToken
                    )
            );
        }

        Cart cart = new Cart();

        cart.setCartToken(
                UUID.randomUUID().toString()
        );

        return cartRepository.save(cart);
    }
}