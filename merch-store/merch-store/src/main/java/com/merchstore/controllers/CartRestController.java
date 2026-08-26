package com.merchstore.controllers;

import com.merchstore.dtos.CartItemCreateDto;
import com.merchstore.dtos.CartOutDto;
import com.merchstore.models.CartResult;
import com.merchstore.models.User;
import com.merchstore.services.CartService;
import com.merchstore.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartRestController {

    private final CartService cartService;
    private final UserService userService;

    public CartRestController(
            CartService cartService,
            UserService userService) {

        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping
    public CartOutDto getCart(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {

        User currentUser =
                getCurrentUser(authentication);

        String cartToken =
                getCartToken(request);

        CartResult result =
                cartService.getOrCreateCart(
                        currentUser,
                        cartToken
                );

        if (result.isNewGuestCart()) {

            addCartCookie(
                    response,
                    result.getCartToken()
            );
        }

        return result.getCart();
    }

    @PostMapping("/items")
    public CartOutDto addItem(
            Authentication authentication,
            HttpServletRequest request,
            @RequestBody CartItemCreateDto cartItemCreateDto) {

        User currentUser =
                getCurrentUser(authentication);

        String cartToken =
                getCartToken(request);

        return cartService.addItem(
                currentUser,
                cartToken,
                cartItemCreateDto
        );
    }

    @PutMapping("/items/{itemId}")
    public CartOutDto updateQuantity(
            @PathVariable Long itemId,
            @RequestParam Integer quantity,
            Authentication authentication,
            HttpServletRequest request) {

        User currentUser =
                getCurrentUser(authentication);

        String cartToken =
                getCartToken(request);

        return cartService.updateQuantity(
                currentUser,
                cartToken,
                itemId,
                quantity
        );
    }

    @DeleteMapping("/items/{itemId}")
    public CartOutDto deleteItem(
            @PathVariable Long itemId,
            Authentication authentication,
            HttpServletRequest request) {

        User currentUser =
                getCurrentUser(authentication);

        String cartToken =
                getCartToken(request);

        return cartService.deleteItem(
                currentUser,
                cartToken,
                itemId
        );
    }

    @DeleteMapping
    public CartOutDto clearCart(
            Authentication authentication,
            HttpServletRequest request) {

        User currentUser =
                getCurrentUser(authentication);

        String cartToken =
                getCartToken(request);

        return cartService.clearCart(
                currentUser,
                cartToken
        );
    }

    private User getCurrentUser(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {

            return null;
        }

        return userService.getUserByUsername(
                authentication.getName()
        );
    }

    private String getCartToken(
            HttpServletRequest request) {

        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {

            if ("cartToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private void addCartCookie(
            HttpServletResponse response,
            String cartToken) {

        Cookie cookie = new Cookie(
                "cartToken",
                cartToken
        );

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);

        response.addCookie(cookie);
    }
}