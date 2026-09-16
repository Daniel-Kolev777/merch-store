package com.merchstore.services;

import com.merchstore.models.Order;

public interface EmailService {

    void sendNewOrderEmail(Order order);

    void sendOrderCreatedEmail(Order order);

    void sendOrderShippedEmail(Order order);
}