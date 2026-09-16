package com.merchstore.dtos.econt;

import java.math.BigDecimal;

public class EcontDeliveryPriceRequestDto {

    private String customerName;
    private String customerPhone;
    private String officeCode;
    private BigDecimal weight;

    public EcontDeliveryPriceRequestDto() {
    }

    public EcontDeliveryPriceRequestDto(
            String customerName,
            String customerPhone,
            String officeCode,
            BigDecimal weight
    ) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.officeCode = officeCode;
        this.weight = weight;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}