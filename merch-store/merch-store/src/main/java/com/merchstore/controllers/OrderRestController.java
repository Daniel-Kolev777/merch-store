package com.merchstore.controllers;

import com.merchstore.dtos.order.OrderCreateDto;
import com.merchstore.dtos.order.OrderOutDto;
import com.merchstore.models.User;
import com.merchstore.services.OrderService;
import com.merchstore.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderRestController(
            OrderService orderService,
            UserService userService) {

        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public List<OrderOutDto> getAll(
            Authentication authentication,
            HttpServletRequest request) {

        User currentUser =
                getCurrentUser(authentication);

        String cartToken =
                getCartToken(request);

        return orderService.getAll(
                currentUser,
                cartToken
        );
    }

    @PostMapping
    public OrderOutDto createOrder(
            Authentication authentication,
            HttpServletRequest request,
            @Valid @RequestBody OrderCreateDto orderCreateDto) {

        User currentUser =
                getCurrentUser(authentication);

        String cartToken =
                getCartToken(request);

        return orderService.createOrder(
                currentUser,
                cartToken,
                orderCreateDto
        );
    }

    @GetMapping("/{orderId}")
    public OrderOutDto getById(
            Authentication authentication,
            HttpServletRequest request,
            @PathVariable Long orderId) {

        User currentUser =
                getCurrentUser(authentication);

        String cartToken =
                getCartToken(request);

        return orderService.getById(
                currentUser,
                cartToken,
                orderId
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

    @PatchMapping("/{orderId}/ship")
    public OrderOutDto shipOrder(
            Authentication authentication,
            @PathVariable Long orderId
    ) {

        User currentUser =
                getCurrentUser(authentication);

        return orderService.shipOrder(
                currentUser,
                orderId
        );
    }
}
