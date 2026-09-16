package com.merchstore.services;

import com.merchstore.dtos.order.OrderCreateDto;
import com.merchstore.dtos.order.OrderOutDto;
import com.merchstore.models.User;

import java.util.List;

public interface OrderService {
    OrderOutDto createOrder(User currentUser,
                            String cartToken,
                            OrderCreateDto orderCreateDto
    );

    OrderOutDto getById(
            User currentUser,
            String cartToken,
            Long orderId
    );

    List<OrderOutDto> getAll(
            User currentUser,
            String cartToken
    );

    OrderOutDto shipOrder(User currentUser, Long orderId);
}