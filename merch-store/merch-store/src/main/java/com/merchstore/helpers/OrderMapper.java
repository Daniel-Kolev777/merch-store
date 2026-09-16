package com.merchstore.helpers;

import com.merchstore.dtos.order.OrderItemOutDto;
import com.merchstore.dtos.order.OrderOutDto;
import com.merchstore.models.Order;
import com.merchstore.models.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class OrderMapper {

    private static final BigDecimal EUR_TO_BGN_RATE =
            new BigDecimal("1.95583");

    public OrderOutDto fromOrderToOutDto(Order order) {

        List<OrderItemOutDto> items =
                order.getItems()
                        .stream()
                        .map(this::fromOrderItemToOutDto)
                        .toList();

        BigDecimal subtotalBgn =
                convertEurToBgn(order.getSubtotal());

        BigDecimal deliveryPriceBgn =
                convertEurToBgn(order.getDeliveryPrice());

        BigDecimal totalPriceBgn =
                convertEurToBgn(order.getTotalPrice());

        return new OrderOutDto(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getCustomerPhone(),
                order.getCity(),
                order.getAddress(),
                order.getCreatedAt(),
                order.getStatus(),
                items,
                order.getPaymentMethod(),
                order.getDeliveryMethod(),

                // EUR
                order.getSubtotal(),
                order.getDeliveryPrice(),
                order.getTotalPrice(),

                // BGN
                subtotalBgn,
                deliveryPriceBgn,
                totalPriceBgn,

                order.getEcontOfficeCode(),
                order.getEcontOfficeName()
        );
    }

    private OrderItemOutDto fromOrderItemToOutDto(
            OrderItem orderItem
    ) {

        return new OrderItemOutDto(
                orderItem.getProduct().getName(),
                orderItem.getPrice(),
                orderItem.getQuantity(),
                orderItem.getSize()
        );
    }

    private BigDecimal convertEurToBgn(
            BigDecimal eurAmount
    ) {

        if (eurAmount == null) {
            return null;
        }

        return eurAmount
                .multiply(EUR_TO_BGN_RATE)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }
}