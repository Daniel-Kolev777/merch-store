package com.merchstore.dtos.order;

import com.merchstore.models.enums.DeliveryMethod;
import com.merchstore.models.enums.OrderStatus;
import com.merchstore.models.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderOutDto {

    private Long id;

    private String customerName;
    private String customerEmail;
    private String customerPhone;

    private String city;
    private String address;

    private LocalDateTime createdAt;

    private OrderStatus status;

    private List<OrderItemOutDto> items;

    private PaymentMethod paymentMethod;
    private DeliveryMethod deliveryMethod;

    // EUR
    private BigDecimal subtotal;
    private BigDecimal deliveryPrice;
    private BigDecimal totalPrice;

    // BGN
    private BigDecimal subtotalBgn;
    private BigDecimal deliveryPriceBgn;
    private BigDecimal totalPriceBgn;

    private String econtOfficeCode;
    private String econtOfficeName;

    public OrderOutDto() {
    }

    public OrderOutDto(
            Long id,
            String customerName,
            String customerEmail,
            String customerPhone,
            String city,
            String address,
            LocalDateTime createdAt,
            OrderStatus status,
            List<OrderItemOutDto> items,
            PaymentMethod paymentMethod,
            DeliveryMethod deliveryMethod,
            BigDecimal subtotal,
            BigDecimal deliveryPrice,
            BigDecimal totalPrice,
            BigDecimal subtotalBgn,
            BigDecimal deliveryPriceBgn,
            BigDecimal totalPriceBgn,
            String econtOfficeCode,
            String econtOfficeName
    ) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.city = city;
        this.address = address;
        this.createdAt = createdAt;
        this.status = status;
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.subtotal = subtotal;
        this.deliveryPrice = deliveryPrice;
        this.totalPrice = totalPrice;
        this.subtotalBgn = subtotalBgn;
        this.deliveryPriceBgn = deliveryPriceBgn;
        this.totalPriceBgn = totalPriceBgn;
        this.econtOfficeCode = econtOfficeCode;
        this.econtOfficeName = econtOfficeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemOutDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemOutDto> items) {
        this.items = items;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getSubtotalBgn() {
        return subtotalBgn;
    }

    public void setSubtotalBgn(BigDecimal subtotalBgn) {
        this.subtotalBgn = subtotalBgn;
    }

    public BigDecimal getDeliveryPriceBgn() {
        return deliveryPriceBgn;
    }

    public void setDeliveryPriceBgn(BigDecimal deliveryPriceBgn) {
        this.deliveryPriceBgn = deliveryPriceBgn;
    }

    public BigDecimal getTotalPriceBgn() {
        return totalPriceBgn;
    }

    public void setTotalPriceBgn(BigDecimal totalPriceBgn) {
        this.totalPriceBgn = totalPriceBgn;
    }

    public String getEcontOfficeCode() {
        return econtOfficeCode;
    }

    public void setEcontOfficeCode(String econtOfficeCode) {
        this.econtOfficeCode = econtOfficeCode;
    }

    public String getEcontOfficeName() {
        return econtOfficeName;
    }

    public void setEcontOfficeName(String econtOfficeName) {
        this.econtOfficeName = econtOfficeName;
    }
}