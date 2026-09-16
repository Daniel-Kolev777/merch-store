package com.merchstore.dtos.order;

import com.merchstore.models.enums.DeliveryMethod;
import com.merchstore.models.enums.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class OrderCreateDto {

    @NotBlank(message = "Customer name is required!")
    private String customerName;

    @NotBlank(message = "Customer email is required!")
    @Email(message = "Invalid email format!")
    private String customerEmail;

    @NotBlank(message = "Customer phone is required!")
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Phone number must contain exactly 10 digits!"
    )
    private String customerPhone;

    private String city;

    private String address;

    @NotNull(message = "Payment method is required!")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Delivery method is required!")
    private DeliveryMethod deliveryMethod;

    private String econtOfficeCode;

    public OrderCreateDto() {
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

    public String getEcontOfficeCode() {
        return econtOfficeCode;
    }

    public void setEcontOfficeCode(String econtOfficeCode) {
        this.econtOfficeCode = econtOfficeCode;
    }
}