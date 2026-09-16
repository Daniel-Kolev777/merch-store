package com.merchstore.services;

import com.merchstore.dtos.cart.CartItemCreateDto;
import com.merchstore.dtos.cart.CartOutDto;
import com.merchstore.exceptions.AuthorizationException;
import com.merchstore.exceptions.BadRequestException;
import com.merchstore.exceptions.EntityNotFoundException;
import com.merchstore.helpers.CartMapper;
import com.merchstore.models.Cart;
import com.merchstore.models.CartItem;
import com.merchstore.models.CartResult;
import com.merchstore.models.Product;
import com.merchstore.models.User;
import com.merchstore.models.enums.Size;
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

        if (cartItemCreateDto.getQuantity() == null
                || cartItemCreateDto.getQuantity() <= 0) {

            throw new BadRequestException(
                    "Quantity must be greater than 0!"
            );
        }

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

        validateProductCanBePurchased(product);

        Size size =
                resolveSize(
                        product,
                        cartItemCreateDto.getSize()
                );

        CartItem cartItem =
                cartItemRepository
                        .findByCartIdAndProductIdAndSize(
                                cart.getId(),
                                product.getId(),
                                size
                        )
                        .orElse(null);

        validateTotalProductQuantityInCart(
                cart,
                product,
                cartItemCreateDto.getQuantity()
        );

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

            cartItem.setSize(size);

            cart.getItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);

        return cartMapper.fromCartToOutDto(
                cart
        );
    }

    @Override
    public CartOutDto updateQuantity(
            User currentUser,
            String cartToken,
            Long itemId,
            Integer quantity) {

        if (quantity == null
                || quantity <= 0) {

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
                cartItemRepository.findById(
                        itemId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "CartItem",
                                "id",
                                String.valueOf(itemId)
                        )
                );

        if (!cartItem.getCart()
                .getId()
                .equals(cart.getId())) {

            throw new AuthorizationException(
                    "CartItem does not belong to this cart!"
            );
        }

        Product product =
                cartItem.getProduct();

        validateProductCanBePurchased(product);

        validateSelectedSize(
                product,
                cartItem.getSize()
        );

        validateTotalProductQuantityForUpdate(
                cart,
                product,
                cartItem,
                quantity
        );

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        return cartMapper.fromCartToOutDto(
                cart
        );
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
                cartItemRepository.findById(
                        itemId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "CartItem",
                                "id",
                                String.valueOf(itemId)
                        )
                );

        if (!cartItem.getCart()
                .getId()
                .equals(cart.getId())) {

            throw new AuthorizationException(
                    "CartItem does not belong to this cart!"
            );
        }

        cartItemRepository.delete(cartItem);

        cart.getItems().remove(cartItem);

        return cartMapper.fromCartToOutDto(
                cart
        );
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

        return cartMapper.fromCartToOutDto(
                cart
        );
    }

    private void validateProductCanBePurchased(
            Product product) {

        if (!product.isActive()) {

            throw new BadRequestException(
                    "Product is not available!"
            );
        }

        if (product.getQuantity() == null
                || product.getQuantity() <= 0) {

            throw new BadRequestException(
                    "Product is out of stock!"
            );
        }
    }

    private void validateTotalProductQuantityInCart(
            Cart cart,
            Product product,
            int quantityToAdd) {

        int currentQuantityInCart =
                cart.getItems()
                        .stream()
                        .filter(item ->
                                item.getProduct()
                                        .getId()
                                        .equals(product.getId())
                        )
                        .mapToInt(CartItem::getQuantity)
                        .sum();

        int finalQuantity =
                currentQuantityInCart
                        + quantityToAdd;

        if (finalQuantity > product.getQuantity()) {

            throw new BadRequestException(
                    "Not enough product quantity in stock! Available quantity: "
                            + product.getQuantity()
            );
        }
    }

    private void validateTotalProductQuantityForUpdate(
            Cart cart,
            Product product,
            CartItem cartItemBeingUpdated,
            int newQuantity) {

        int otherItemsQuantity =
                cart.getItems()
                        .stream()
                        .filter(item ->
                                item.getProduct()
                                        .getId()
                                        .equals(product.getId())
                        )
                        .filter(item ->
                                !item.getId()
                                        .equals(
                                                cartItemBeingUpdated.getId()
                                        )
                        )
                        .mapToInt(CartItem::getQuantity)
                        .sum();

        int finalQuantity =
                otherItemsQuantity
                        + newQuantity;

        if (finalQuantity > product.getQuantity()) {

            throw new BadRequestException(
                    "Not enough product quantity in stock! Available quantity: "
                            + product.getQuantity()
            );
        }
    }

    private Size resolveSize(
            Product product,
            String size) {

        if (!product.isHasSizes()) {

            if (size != null
                    && !size.isBlank()) {

                throw new BadRequestException(
                        "This product does not use sizes!"
                );
            }

            return null;
        }

        Size parsedSize =
                parseSize(size);

        validateSelectedSize(
                product,
                parsedSize
        );

        return parsedSize;
    }

    private void validateSelectedSize(
            Product product,
            Size size) {

        if (!product.isHasSizes()) {
            return;
        }

        if (size == null) {

            throw new BadRequestException(
                    "Size is required for this product!"
            );
        }

        if (product.getAvailableSizes() == null
                || !product.getAvailableSizes()
                .contains(size)) {

            throw new BadRequestException(
                    "Selected size is not available!"
            );
        }
    }

    private Size parseSize(
            String size) {

        if (size == null
                || size.isBlank()) {

            throw new BadRequestException(
                    "Size is required for this product!"
            );
        }

        try {

            return Size.valueOf(
                    size.trim()
                            .toUpperCase()
            );

        } catch (IllegalArgumentException exception) {

            throw new BadRequestException(
                    "Invalid size: " + size
            );
        }
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

                return cartRepository.save(
                        cart
                );
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
                UUID.randomUUID()
                        .toString()
        );

        return cartRepository.save(
                cart
        );
    }
}