package com.merchstore.dtos.econt;

public class EcontOfficeDto {

    private Long id;
    private String code;
    private String name;
    private String city;
    private String address;

    public EcontOfficeDto() {
    }

    public EcontOfficeDto(
            Long id,
            String code,
            String name,
            String city,
            String address
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.city = city;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}