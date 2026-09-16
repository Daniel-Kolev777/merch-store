package com.merchstore.dtos.econt;

public class EcontApiAddressDto {

    private EcontApiCityDto city;
    private String fullAddress;

    public EcontApiAddressDto() {
    }

    public EcontApiCityDto getCity() {
        return city;
    }

    public void setCity(EcontApiCityDto city) {
        this.city = city;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}